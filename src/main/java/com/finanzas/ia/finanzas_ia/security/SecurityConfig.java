package com.finanzas.ia.finanzas_ia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .requestMatchers("/login-user", "/register-user", "/", "/css/**", "/js/**", "/img/**","/scss/**", "javascript").permitAll() // Cambié antMatchers por requestMatchers
                .anyRequest().authenticated() // El resto de las rutas requieren autenticación
            .and()
            .formLogin()
                .loginPage("/login-user") // Página personalizada para login
                .loginProcessingUrl("/login") // URL de procesamiento del formulario
                .defaultSuccessUrl("/dashboard", true) // Redirige al dashboard al loguearse
                .failureUrl("/login-user?error=true") // Redirige a la página de login en caso de error
                .permitAll() // Permite el acceso a la página de login sin autenticación
            .and()
            .logout()
                .logoutUrl("/logout") // URL de logout
                .logoutSuccessUrl("/login-user?logout=true") // Redirección después del logout
                .permitAll(); // Permite el acceso al logout sin autenticación

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para la codificación de contraseñas
    }
}
