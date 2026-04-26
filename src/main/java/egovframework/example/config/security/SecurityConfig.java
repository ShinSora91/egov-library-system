package egovframework.example.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/loan/**").authenticated()
                .requestMatchers("/", "/member/register", "/member/login", 
                                 "/member/checkId", "/book/list", "/book/detail/**",
                                 "/book/**", "/css/**", "/js/**",
                                 "/swagger-ui/**", "/swagger-ui.html",
                                 "/v3/api-docs/**", "/v3/api-docs",
                                 "/chat/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/member/login")
                .loginProcessingUrl("/member/loginProcess")
                .defaultSuccessUrl("/", false)
                .failureUrl("/member/login?error=true")
                .usernameParameter("memberId")
                .passwordParameter("memberPw")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
            );

            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
