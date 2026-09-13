package com.hospital.schedulingservice.infra.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            Converter<Jwt, Collection<GrantedAuthority>> keycloakAuthoritiesConverter,
            SecurityErrorHandler securityErrorHandler)
            throws Exception {

        JwtAuthenticationConverter authenticationConverter
                = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                keycloakAuthoritiesConverter
        );

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/appointments").hasAnyRole(
                "DOCTOR", "NURSE"
        )
                .requestMatchers(HttpMethod.GET, "/api/v1/appointments").hasAnyRole(
                "DOCTOR", "NURSE", "PATIENT"
        )
                .requestMatchers(HttpMethod.GET, "/api/v1/appointments/**").hasAnyRole(
                "DOCTOR", "NURSE", "PATIENT"
        )
                .requestMatchers(HttpMethod.PUT, "/api/v1/appointments/**").hasAnyRole(
                "DOCTOR", "NURSE"
        )
                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                .jwtAuthenticationConverter(authenticationConverter)
        ))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(securityErrorHandler)
                .accessDeniedHandler(securityErrorHandler)
                );

        return http.build();
    }

    @Bean
    Converter<Jwt, Collection<GrantedAuthority>> keycloakAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter
                = new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>(
                    scopeConverter.convert(jwt)
            );

            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null) {
                return authorities;
            }

            Object roles = realmAccess.get("roles");
            if (roles instanceof Collection<?> roleCollection) {
                roleCollection.stream()
                        .map(Object::toString)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .forEach(authorities::add);
            }

            return authorities;
        };
    }
}
