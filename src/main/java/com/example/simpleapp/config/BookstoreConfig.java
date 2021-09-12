package com.example.simpleapp.config;

import com.example.simpleapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class BookstoreConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserRepository userRepository;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.httpBasic()
                .and()
                 .authorizeRequests()
                    .antMatchers("/","/test").permitAll()
                    //.anyRequest().authenticated()
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .permitAll()
                    .and()
                .logout()
                .permitAll();*/
        http.csrf().disable();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        /*List<UserAccount> userAccountsList = userRepository.getListOfUserAccount();
        for(UserAccount u : userAccountsList){
            auth.inMemoryAuthentication().withUser(u.getUsername())
                    .password(passwordEncoder().encode(u.getPassword())).roles("USER");
        }
        auth.inMemoryAuthentication().withUser("user")
                .password(passwordEncoder().encode("password")).roles("USER");*/
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
