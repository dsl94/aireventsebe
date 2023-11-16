package com.airevents.security.config;

import com.airevents.security.JwtAuthenticationEntryPoint;
import com.airevents.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/v2/api-docs",           // swagger
                        "/webjars/**",            // swagger-ui webjars
                        "/swagger-resources/**",  // swagger-ui resources
                        "/configuration/**",      // swagger configuration
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/sys-admin/*").hasAnyRole("SYSTEM_ADMIN")
                .antMatchers("/api/profile/**").authenticated()
                .antMatchers("/api/race/**").authenticated()
                .antMatchers("/api/challenge/**").authenticated()
                .antMatchers("/api/race-report/**").authenticated()
//                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/airline/user/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/airline/admin/**").hasRole("ADMIN")
//                .antMatchers("/route/user/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/route/admin/**").hasRole("ADMIN")
//                .antMatchers("/booking/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/statistics/**").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/aircraft/**").hasRole("ADMIN")
//                .antMatchers("/account").authenticated()

                //allow anonymous auth requests
                .antMatchers("/api/flight/**").permitAll()
                .antMatchers("/api/strava/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/password/**").permitAll()
                .antMatchers("/api/reset/**").hasAnyRole("SYSTEM_ADMIN")

                //authenticated requests
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

}
