package com.citasmedica.backend.backendcitasmedicas.auth.filter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.citasmedica.backend.backendcitasmedicas.models.entities.Pacient;
import com.citasmedica.backend.backendcitasmedicas.services.PacientService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.citasmedica.backend.backendcitasmedicas.auth.TokenJwtConfig.*;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
     private PacientService pacientService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, PacientService pacientService) {
        this.authenticationManager = authenticationManager;
        this.pacientService = pacientService; 
        
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        Pacient pacient = null;
        Long dni = null;
        String password = null;
        String nombreApe= null;
       
        try {
            pacient = new ObjectMapper().readValue(request.getInputStream(), Pacient.class);
            dni = pacient.getDni();
            password = pacient.getPassword();
            nombreApe = pacient.getNombreApe(); 
            logger.info("Dni desde reques inputStream(raw)" + dni);
            logger.info("password desde reques inputStream(raw)" + password);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dni, password);
        return authenticationManager.authenticate(authToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
    Long dni = Long.parseLong(((User) authResult.getPrincipal()).getUsername());
    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_MEDICO"));

    // Find pacient by dni and handle Optional
    Optional<Pacient> optionalPacient = pacientService.findByDni(dni);
    
    if (optionalPacient.isPresent()) {
        Pacient pacient = optionalPacient.get();
        String nombreApe = pacient.getNombreApe();

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        claims.put("isAdmin", isAdmin);
        claims.put("dni", dni);
        claims.put("nombreApe", nombreApe); 

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(dni.toString())
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3600000))
                .compact();

        response.addHeader("Authorization", "Bearer " + token);
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hola %s, has iniciado sesión con éxito!", nombreApe));
        body.put("dni", dni);
        body.put("nombreApe", nombreApe);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
    } else {
        // Handle case when pacient is not found
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Paciente no encontrado");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
    }
}

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error de autenticacion");
        body.put("error", failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");

    }

}
