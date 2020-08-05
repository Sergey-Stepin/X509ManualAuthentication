/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.steps.prob.x509auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author stepin
 */
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new MyX509Filter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/test").hasAnyAuthority("BEST_CLIENTS")
                .antMatchers("/", "/*", "/**").permitAll()
                .anyRequest().authenticated()
                
                //.and().x509().userDetailsService(userDetailsService())
                ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return (String username) -> {
            return new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList("BEST_CLIENTS"));
        };
    }
}
