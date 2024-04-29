package com.first.project.FirstBootProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class MyConfig
{

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource)
    {
        System.out.println("Creating UserDetailsService bean...");

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email,password,enabled from user where email=?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select role from user where email=?"
        );

        System.out.println("UserDetailsService bean created successfully.");
        return jdbcUserDetailsManager;
    }

   /* @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }*/


    /*@Bean
    public void configure(AuthenticationManagerBuilder auth)throws Exception
    {
        auth.authenticationProvider(authenticationProvider());
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(configurer->
                configurer.requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/","/signup","about").permitAll()
                .anyRequest().authenticated()
                ).formLogin(form ->
                        form.loginPage("/showLoginForm")
                                .usernameParameter("email")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout ->
                        logout.permitAll().logoutSuccessUrl("/")
                )
                .exceptionHandling(configurer->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }



}
