package HospitalMS.config;

import HospitalMS.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/auth/**")
                        .permitAll()

                        // ==========================
                        // PATIENT APIs
                        // ==========================

                        .requestMatchers(HttpMethod.POST,
                                "/patients/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/patients/**")
                        .hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(HttpMethod.PUT,
                                "/patients/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/patients/**")
                        .hasRole("ADMIN")

                        // ==========================
                        // DOCTOR APIs
                        // ==========================

                        .requestMatchers(HttpMethod.POST,
                                "/doctors/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/doctors/**")
                        .hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(HttpMethod.PUT,
                                "/doctors/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/doctors/**")
                        .hasRole("ADMIN")

                        // ==========================
                        // APPOINTMENTS
                        // ==========================

                        .requestMatchers(HttpMethod.POST,
                                "/appointments/**")
                        .hasAnyRole("ADMIN", "PATIENT")

                        .requestMatchers(HttpMethod.GET,
                                "/appointments/**")
                        .hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(HttpMethod.PUT,
                                "/appointments/*/complete")
                        .hasAnyRole("ADMIN", "DOCTOR")

                        .requestMatchers(HttpMethod.PUT,
                                "/appointments/*/cancel")
                        .hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                        .requestMatchers(HttpMethod.DELETE,
                                "/appointments/**")
                        .hasRole("ADMIN")

                        // ==========================
                        // BILLS
                        // ==========================

                        .requestMatchers(HttpMethod.POST,
                                "/bills/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/bills/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/bills/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/bills/**")
                        .hasAnyRole("ADMIN", "PATIENT")

                        .anyRequest()
                        .authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}