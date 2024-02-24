package ms.toy.my_service.config;

import lombok.RequiredArgsConstructor;
import ms.toy.my_service.jwt.CustomAccessDeniedHandler;
import ms.toy.my_service.jwt.CustomAuthenticationEntryPoint;
import ms.toy.my_service.jwt.JwtFilter;
import ms.toy.my_service.jwt.TokenProvider;
import ms.toy.my_service.service.TokenInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final TokenInfoService tokenInfoService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(FormLoginConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/{version}/swagger/**", "/api/{version}/login/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(tokenProvider, tokenInfoService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> exception
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
