package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig {

	@Bean
	public UserDetailsService getUserDetailsService() {
		
		return new UserDetailsServiceImpl();
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	//configure method..
	
	 
	 @Bean
		public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		 httpSecurity
		 	.csrf()
			.disable()
			.authorizeHttpRequests()
			.requestMatchers("/admin/**")
			.hasRole("ADMIN")
			.requestMatchers("/user/**")
			.hasRole("USER")
			.requestMatchers("/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/signin")
			.loginProcessingUrl("/dologin")
			.defaultSuccessUrl("/",true)
			//***************Login failure*********************************
			.failureUrl("/signin/error");
			return httpSecurity.build();
	
	 }
}
