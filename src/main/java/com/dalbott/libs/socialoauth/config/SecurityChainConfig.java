package com.dalbott.libs.socialoauth.config;

import com.dalbott.libs.socialoauth.security.HttpCookieAuthorizationRepository;
import com.dalbott.libs.socialoauth.security.TokenAuthenticationFilter;
import com.dalbott.libs.socialoauth.security.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.dalbott.libs.socialoauth.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.dalbott.libs.socialoauth.security.service.CustomOAuth2UserService;
import com.dalbott.libs.socialoauth.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityChainConfig  {


    @Autowired
    private HttpCookieAuthorizationRepository httpCookieAuthorizationRepository;

    @Autowired
    private CustomOAuth2UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler failureHandler;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> {
                    auth.requestMatchers("/auth/**", "/oauth2/**").permitAll();
                    auth.requestMatchers(
                            "/",
                            "/error",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.gif",
                            "/**/*.svg",
                            "/**/*.jpg",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js"
                    ).permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //.exceptionHandling(handling -> handling.authenticationEntryPoint(new RestAuthenticationEntryPoint()))

                .oauth2Login(login -> {
                    login.authorizationEndpoint(endpoint -> {
                        endpoint.baseUri("/oauth2/authorize");
                        endpoint.authorizationRequestRepository(this.httpCookieAuthorizationRepository);
                    });
                    login.redirectionEndpoint(endpoint -> {
                        endpoint.baseUri("/oauth2/callback/*");
                    });
                    login.userInfoEndpoint(endpoint -> {
                       endpoint.userService(userService);
                    });
                    login.successHandler(successHandler);
                    login.failureHandler(failureHandler);
                })
                // Add our custom Token based authentication filter
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
