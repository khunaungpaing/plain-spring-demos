# Spring Security Configuration

This repository contains a sample Spring Security configuration setup with Java configuration and XML for data source.

## Dependencies

- [spring-security-core-x.x.x.jar](https://mvnrepository.com/artifact/org.springframework.security/spring-security-core)
- [spring-security-config-x.x.x.jar](https://mvnrepository.com/artifact/org.springframework.security/spring-security-config)
- [spring-security-crypto-x.x.x.jar](https://mvnrepository.com/artifact/org.springframework.security/spring-security-crypto)
- [spring-security-web-x.x.x.jar](https://mvnrepository.com/artifact/org.springframework.security/spring-security-web)
- [spring-security-taglibs-x.x.x.jar](https://mvnrepository.com/artifact/org.springframework.security/spring-security-taglibs) (optional: for JSP)

In this demo, I use version 5.5.2 to ensure compatibility with Spring version 5.3.20. You can find this version attached under [libs/spring-security-libs](https://github.com/khunaungpaing/plain-spring-demos/tree/main/libs/spring-security-libs) within the repository

## Security Configuration

Define security config with Java configuration:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // code...
}
```

## DataSource Configuration
Define DataSource with XML config in `dispatcher-servlet.xml` (or) `application-context.xml` 
(can also be used with Java config):

```xml
<!-- Define DataSource -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/database_name"/>
    <property name="username" value="db_username"/>
    <property name="password" value="db_password"/>
</bean>
```

# Password Encoder Bean
Define BcryptPasswordEncoder Bean:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // default rounds - 10
}
```
# JdbcAuthentication
```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username=?")
            .authoritiesByUsernameQuery("SELECT username, role FROM authorities WHERE username=?")
            .passwordEncoder(passwordEncoder());
    }
```
# Authentication and Authorization
Configure authentication and authorization:
```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .httpBasic().disable()
        .authorizeRequests()
            // add request path that does not need authentication
            .antMatchers("/login", "/logout", "/public/**", "/css/**", "/js/**", "/images/**", "/views/**").permitAll()
            // request path that require specific roles
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            // All other requests require authentication
            .anyRequest().authenticated()
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
```

# Web.xml Configuration
Add filter to web.xml:
```xml
<!-- Spring Security Filter -->
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
Feel free to adjust paths and configurations as needed for your project.
