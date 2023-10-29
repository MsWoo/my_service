package mswoo.toyproject.my_service.config;

import lombok.RequiredArgsConstructor;
import mswoo.toyproject.my_service.config.jwt.JwtAccessDeniedHandler;
import mswoo.toyproject.my_service.config.jwt.JwtAuthenticationEntryPoint;
import mswoo.toyproject.my_service.config.jwt.JwtFilter;
import mswoo.toyproject.my_service.config.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/v1/login/**").permitAll()
                                .requestMatchers("/api/v1/member/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
//                https://iseunghan.tistory.com/302
//                 stateless 세션?
//                 formlogin
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> exception
                                .accessDeniedHandler(new JwtAccessDeniedHandler())
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
