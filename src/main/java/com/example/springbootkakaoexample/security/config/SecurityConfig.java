package com.example.springbootkakaoexample.security.config;

import com.example.springbootkakaoexample.security.filter.KakaoAuthenticationFilter;
import com.example.springbootkakaoexample.security.handler.KakaoAuthenticationAccessHandler;
import com.example.springbootkakaoexample.security.provider.KakaoAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(kakaoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/auth/**").permitAll()
                    .anyRequest().authenticated()
                ;

        http
                .addFilterBefore(kakaoAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .csrf().disable();
    }

    public KakaoAuthenticationFilter kakaoAuthenticationFilter() throws Exception {
        KakaoAuthenticationFilter filter = new KakaoAuthenticationFilter();
        filter.setAuthenticationManager(this.authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new KakaoAuthenticationAccessHandler());
        return filter;
    }
}
