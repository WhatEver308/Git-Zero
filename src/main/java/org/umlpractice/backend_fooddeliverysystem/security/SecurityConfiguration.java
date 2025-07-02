package org.umlpractice.backend_fooddeliverysystem.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.umlpractice.backend_fooddeliverysystem.service.UserDetailsServiceImplement;
import org.umlpractice.backend_fooddeliverysystem.util.filter.WhitelistFilter;

import java.util.List;

/**
 * SecurityConfiguration 类说明
 *
 * @author 刘陈文君
 * @date 2025 /5/28 18:25
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Autowired
    private WhitelistFilter whitelistFilter;

    /**
     * Password encoder password encoder.
     * generates BCryptPasswordEncoder
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsServiceImplement userDetailsServiceImplement) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/debug/ipwhitelist").permitAll() // 允许登录接口公开访问
                        .anyRequest().authenticated() // 其他接口需要认证
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(whitelistFilter, jwtRequestFilter.getClass());
        return http.build();
    }

    /**
     * Authentication manager authentication manager.
     * @param userDetailsService the user details service
     * @param passwordEncoder    the password encoder
     * @return the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsServiceImplement userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        ProviderManager manager = new ProviderManager(List.of(provider));
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoderBean()
    {
        return new BCryptPasswordEncoder();
    }
}
