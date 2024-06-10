package com.citasmedica.backend.backendcitasmedicas.auth.filter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.citasmedica.backend.backendcitasmedicas.auth.SimpleGrantedAuthorityJsonCreator;
import com.citasmedica.backend.backendcitasmedicas.auth.TokenJwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.citasmedica.backend.backendcitasmedicas.auth.TokenJwtConfig.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }
        String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).build()
                    .parseClaimsJws(token)
                    .getBody();
                    Object authoritiesClaims = claims.get("authorities");

                    String dni = claims.getSubject();
                    String nombreApe = claims.get("nombreApe", String.class);
        
                    // Añadir el ID del paciente y el nombre completo como atributos en la solicitud
                    request.setAttribute("nombreApe", nombreApe);
                    request.setAttribute("dni", dni);
        
                    Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                            new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dni, null,
                            authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                } catch (JwtException e) {
                    Map<String, String> body = new HashMap<>();
                    body.put("error", e.getMessage());
                    body.put("error", "Token invalido");
        
                    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                    response.setStatus(401);
                    response.setContentType("application/json");
                }
            }
        }