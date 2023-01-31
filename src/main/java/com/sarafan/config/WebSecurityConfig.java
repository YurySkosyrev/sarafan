package com.sarafan.config;

import com.sarafan.security.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    private final CustomOidcUserService customOidcUserService;

    public WebSecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/**","/login**","/logout**", "/js/**", "/error**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().clearAuthentication(true)
                .deleteCookies().invalidateHttpSession(true)
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(customOidcUserService);
        return http.build();
    }
}
