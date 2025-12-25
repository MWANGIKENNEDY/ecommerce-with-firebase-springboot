package com.ecommerce.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseTokenFilter firebaseTokenFilter;

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // return http
    // .csrf(AbstractHttpConfigurer::disable)
    // .headers(headers -> headers
    // .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // Critical!
    // )
    // .cors(Customizer.withDefaults()) // <-- REQUIRED
    // .sessionManagement(s ->
    // s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // // 🔥 Correct block for request authorization
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/public/**").permitAll()
    // .requestMatchers("/h2-console/**").permitAll()
    // .anyRequest().authenticated()
    // )
    // // 🔥 Add Firebase token filter before UsernamePasswordAuthenticationFilter
    // .addFilterBefore(firebaseTokenFilter,
    // UsernamePasswordAuthenticationFilter.class)
    // .build();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 🔥 Require authentication for cart and order endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .anyRequest().permitAll())
                // 🔥 Add Firebase token filter
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
