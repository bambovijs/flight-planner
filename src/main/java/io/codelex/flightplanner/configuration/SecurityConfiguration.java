package io.codelex.flightplanner.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(withDefaults())  // Enable HTTP Basic Authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/testing-api/", "/api/").authenticated()  // Require authentication for API endpoints
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);  // Disable CSRF for simplicity
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("codelex-admin")
//                .password(passwordEncoder().encode("Password123"))
//                .roles("ADMIN")
//                .build());
//        return manager;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
