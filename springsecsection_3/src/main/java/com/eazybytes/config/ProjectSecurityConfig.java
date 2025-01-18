package com.eazybytes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());


//        To customize the that only certain APIs are authenticated, you can use the authorizeHttpRequests method to specify the paths that you want to secure.
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/myAccount","/myLoans","/myCards","/myBalance").authenticated());

//       To permit the APIs which are not need to be secured.
        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/notices","/contact","/error").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService  userDetailsService(){
       UserDetails user =  User.withUsername("user").password("{noop}12345").authorities("read").build();
        UserDetails admin =  User.withUsername("admin").password("{noop}12345").authorities("admin").build();
        return new InMemoryUserDetailsManager(user,admin);
    }

}
