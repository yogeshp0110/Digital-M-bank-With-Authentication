package com.maveric.bank.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomerUserDetailsService userDetailsServiceimpl;
	
	@Autowired
	JwtFilter jwtFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceimpl);
		
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean(name=BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	      
	   http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
	       .and()
	       .csrf().disable()
	       .authorizeRequests()
	            
	       .antMatchers(HttpMethod.POST, "/branches/**","/bankers/**").hasRole("SUPERADMIN") 
	       .antMatchers(HttpMethod.GET,  "/bankers/**").hasRole("SUPERADMIN") 
	       .antMatchers(HttpMethod.DELETE, "/branches/**","/accounts/**","/bankers/**","/customer/**","/transactions/**","loans/**","/credit-cards/**").hasRole("SUPERADMIN")  
		      
	       
	       .antMatchers(HttpMethod.GET,  "/branches/**","/accounts/**","transactions/**","/credit-cards/**").hasAnyRole("ADMIN","SUPERADMIN")	       
	       .antMatchers(HttpMethod.POST,  "/customer/**","loans/**","/credit-cards/**").hasAnyRole("ADMIN","SUPERADMIN")
		     
           
	       .antMatchers(HttpMethod.GET, "/customer/**","/accounts/**","transactions/**","loans/**","/loan-payments/**","/credit-cards/**").hasAnyRole("ADMIN","SUPERADMIN","CUSTOMER")       
	       .antMatchers(HttpMethod.POST, "/transactions/**","/loan-payments/**").hasAnyRole("ADMIN","SUPERADMIN","CUSTOMER")       
			    
	       .antMatchers("/user/login","/user/signup","/user/forgotPassword", "/swagger-ui.html","/swagger-ui/index.html",
	    		   "/swagger-ui/**",  
	               "/v3/api-docs/**", 
	               "/swagger-ui.html",
	               "/favicon.ico"   
	    		   
	    		   )
	       .permitAll()
	       .anyRequest()
	       .authenticated()
	       .and().exceptionHandling()
	       .and()
	       .sessionManagement()
	       .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	   
	   http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}	
}


