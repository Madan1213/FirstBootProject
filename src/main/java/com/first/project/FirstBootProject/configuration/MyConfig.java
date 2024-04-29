package com.first.project.FirstBootProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableWebSecurity
public class MyConfig
{

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource)
    {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        //Define Query to retrieve user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email,password,enabled from user where id=?"
        );
        //Define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select role from user where id=?"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    /*@Bean
    public void configure(AuthenticationManagerBuilder auth)throws Exception
    {
        auth.authenticationProvider(authenticationProvider());
    }*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->
                configurer.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
        ).formLogin(form->
                form.loginPage("/showLoginForm")
                        .loginProcessingUrl("/authenticateTheUser")
                        .permitAll()
                ).logout(logout->
                    logout.permitAll()
                );
        return http.build();
    }
}
