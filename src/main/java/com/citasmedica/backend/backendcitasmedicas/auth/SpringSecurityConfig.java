package com.citasmedica.backend.backendcitasmedicas.auth;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.citasmedica.backend.backendcitasmedicas.auth.filter.JwtAuthenticationFilter;
import com.citasmedica.backend.backendcitasmedicas.auth.filter.JwtValidationFilter;
import com.citasmedica.backend.backendcitasmedicas.services.PacientService;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    @Lazy
    private PacientService pacientService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/pacients", "/citas").permitAll()
                .requestMatchers(HttpMethod.PUT, "/citas/citacurso/{idCita}").hasRole("MEDICO")
                .requestMatchers(HttpMethod.PUT, "/citas/citacurso/updateReceta/{idCita}").hasRole("MEDICO")
                .requestMatchers(HttpMethod.POST, "/citas").hasAnyRole("PACIENT", "MEDICO")
                .requestMatchers(HttpMethod.DELETE, "/citas/{idCita}").hasAnyRole("PACIENT", "MEDICO")
                .requestMatchers(HttpMethod.PUT, "/citas/{idCita}").hasAnyRole("PACIENT", "MEDICO")
                .requestMatchers(HttpMethod.PUT, "/citas/citafinish/{idCita}").hasRole("MEDICO")
                .requestMatchers(HttpMethod.GET, "/citas/fecha/{fecha}/{medico}/{especialidad}").hasAnyRole("PACIENT", "MEDICO")
                .anyRequest().authenticated()
            )
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), pacientService))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
