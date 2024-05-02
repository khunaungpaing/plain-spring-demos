package com.khun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    
    public SecurityConfig(DataSource dataSource) {
    	this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, role FROM authorities WHERE username=?")
            .passwordEncoder(passwordEncoder());
    }


    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
        	.httpBasic().disable()
            .authorizeRequests()
            	.antMatchers("/login", "/logout", "/public/**", "/css/**", "/js/**", "/images/**","/test").permitAll()
	            .anyRequest().authenticated()  // All other requests require authentication
            .and()
            .formLogin()
                .loginPage("/login") // Custom login page URL
                .defaultSuccessUrl("/") // Redirect to home page after successful login
            .and()
            .logout()
                .logoutUrl("/logout") // Custom logout URL
                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
