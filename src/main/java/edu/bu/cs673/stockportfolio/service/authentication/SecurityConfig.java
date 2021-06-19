package edu.bu.cs673.stockportfolio.service.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**********************************************************************************************************************'
 * Spring configuration class that implements the methods that modify Spring's configuration to use our Services. The
 * WebSecurityConfigurerAdapter describes the methods that modify Spring's security configuration.
 *
 * @author Michael Lewis
 *********************************************************************************************************************/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.authenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Allow unauthenticated users to access the signup endpoint. Allow authenticated users access to all endpoints
        http.authorizeRequests()
                .antMatchers("/signup", "/css/*", "/js/*").permitAll()
                .anyRequest().authenticated();

        // permit all requests to the login endpoint
        http.formLogin()
                .loginPage("/login")
                .permitAll();

        // automatically redirect successful logins to the home endpoint
        http.formLogin()
                .defaultSuccessUrl("/home", true);

        http.logout().permitAll();
    }
}