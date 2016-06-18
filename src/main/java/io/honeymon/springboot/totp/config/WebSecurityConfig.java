package io.honeymon.springboot.totp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

import io.honeymon.springboot.totp.security.ExtensibleAuthenticationFailureHandler;
import io.honeymon.springboot.totp.security.ExtensibleUserDetailsAuthenticationProvider;
import io.honeymon.springboot.totp.security.TOTPWebAuthenticationDetailsSource;

/**
 * Spring Security Configuration
 * 
 * @author honeymon
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	public static final String JSESSIONID = "JSESSIONID";
    @Autowired
    UserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	IGoogleAuthenticator googleAuthenticator() {
		return new GoogleAuthenticator();
	}
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	     http
         .authorizeRequests()
         .antMatchers("/user/add").permitAll()
         .antMatchers("/logout").permitAll()
         .antMatchers(HttpMethod.GET, "/login").permitAll()
         .antMatchers("/h2-console/**").permitAll()
         .anyRequest().fullyAuthenticated()
         .and()
         .formLogin().authenticationDetailsSource(new TOTPWebAuthenticationDetailsSource())
         .loginPage("/login").defaultSuccessUrl("/")
         .failureUrl("/login?error").failureHandler(new ExtensibleAuthenticationFailureHandler()).permitAll()
         .and()
         .logout()
         .logoutSuccessUrl("/login?logout")
         .deleteCookies(JSESSIONID).permitAll()
         .and()
         	.headers().frameOptions().disable()
         .and()
         	.csrf().disable();
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider())
                .inMemoryAuthentication()
                .withUser("user").password("user").roles("USER");
    }
	
	@Bean
    AuthenticationProvider authenticationProvider() {
        ExtensibleUserDetailsAuthenticationProvider authenticationProvider = new ExtensibleUserDetailsAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setAuthenticator(googleAuthenticator());
        return authenticationProvider;
    }
}
