package com.first.project.FirstBootProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class MyConfig
{

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource)
    {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        //Define Query to retrieve user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email,password,enabled from user where email=?"
        );
        //Define query to retrieve the authorities/roles by username
       jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select email,role from user where email=?"
        );
        return jdbcUserDetailsManager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Return a NoOpPasswordEncoder which doesn't encode passwords
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->
                configurer.
                        requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/","/signup","/home").permitAll()
                        .anyRequest().permitAll()
        ).formLogin(form->
                form.loginPage("/showLoginForm")
                        .usernameParameter("email")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/user/index")
                        .permitAll()
        ).logout(logout->
                logout.logoutUrl("/logout").
                        deleteCookies("JSESSIONID").permitAll()
        ).exceptionHandling(configure->
                configure.accessDeniedPage("/access-denied")).csrf(csrf->csrf.disable());
        return http.build();
    }

    /*@Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("email")
                .password("password")
                .roles("USER").build();
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource)
    }*/

    /*@Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

     @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }*/

    /*@Bean
    public UserDetailsService userDetailsService(DataSource dataSource)
    {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        //Define Query to retrieve user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select email,password,enabled,role from user where email=?"
        );
        //Define query to retrieve the authorities/roles by username
       jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select role from user where email=?"
        );
        return jdbcUserDetailsManager;
    }*/

    /*@Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }*/


    /*@Bean
    public void configure(AuthenticationManagerBuilder auth)throws Exception
    {
        auth.authenticationProvider(authenticationProvider());
    }*/

}
