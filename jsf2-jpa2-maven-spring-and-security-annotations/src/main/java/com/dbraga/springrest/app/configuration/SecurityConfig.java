package com.dbraga.springrest.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("userDetailsService")
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("dba").password("dba").roles("ADMIN","DBA");
//	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/admin.xhtml").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/dba.xhtml").access("hasRole('ROLE_DBA')")
			.antMatchers("/javax.faces.resource/**").permitAll()
			.antMatchers("/resources/**").permitAll()
			.anyRequest().authenticated()
		
		.and().formLogin()	//login configuration
                .loginPage("/customLogin.xhtml").permitAll()
                .loginProcessingUrl("/appLogin")
                .usernameParameter("app_username")
                .passwordParameter("app_password")
                .defaultSuccessUrl("/home.xhtml")	
        
		.and()		//logout configuration
			.logout()
			.logoutUrl("/appLogout")
			.logoutSuccessUrl("/customLogin.xhtml")
			
		.and()		//Access Denied
			.exceptionHandling()
			.accessDeniedPage("/accessDenied.xhtml");

		
	} 

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
} 