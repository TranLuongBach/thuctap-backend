package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // ===== CONSTANTS =====
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private static final String AUTH_API = "/api/auth/**";
    private static final String TASK_API = "/api/tasks/**";
    private static final String PROJECT_API = "/api/projects/**";

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> configureAuthorization(auth))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void configureAuthorization(
            org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        auth
                // ===== PUBLIC =====
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .requestMatchers(AUTH_API).permitAll()

                // ===== TASK =====
                .requestMatchers(HttpMethod.GET, TASK_API)
                .hasAnyRole("USER", "MANAGER")

                .requestMatchers(HttpMethod.POST, TASK_API)
                .hasAnyRole("USER", "MANAGER")

                .requestMatchers(HttpMethod.PUT, TASK_API)
                .hasRole("MANAGER")

                .requestMatchers(HttpMethod.DELETE, TASK_API)
                .hasRole("MANAGER")

                // ===== PROJECT =====
                .requestMatchers(HttpMethod.GET, PROJECT_API)
                .hasAnyRole("USER", "MANAGER")

                .requestMatchers(HttpMethod.POST, PROJECT_API)
                .hasRole("MANAGER")

                .requestMatchers(HttpMethod.PUT, PROJECT_API)
                .hasRole("MANAGER")

                .requestMatchers(HttpMethod.DELETE, PROJECT_API)
                .hasRole("MANAGER")

                // ===== DEFAULT =====
                .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}