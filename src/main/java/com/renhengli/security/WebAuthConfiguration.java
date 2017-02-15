package com.renhengli.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebAuthConfiguration extends WebSecurityConfigurerAdapter {
	private static Logger logger = Logger.getLogger(WebAuthConfiguration.class);
	
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug(
				"Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");

		// http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
		http.authorizeRequests().antMatchers("/home/*", "/","/static/**").permitAll()
			.anyRequest().authenticated().and().formLogin()
			.usernameParameter("username").passwordParameter("password").loginProcessingUrl("/login")
			.loginPage("/login").and().logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/login")
			//.logoutSuccessHandler(logoutSuccessHandler)
			.invalidateHttpSession(true)
			//.addLogoutHandler(logoutHandler)
			.deleteCookies(new String[] { "currentUser" }).and().rememberMe();
	}
	
//	@Autowired  
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {  
//	    auth.jdbcAuthentication().dataSource(dataSource)  
//	    .usersByUsernameQuery("select username,password, enabled from users where username=?")  
//	    .authoritiesByUsernameQuery("select username, role from user_roles where username=?");  
//	}  
	
//	@Autowired  
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {  
//	    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();  
//	    auth.eraseCredentials(false).userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);  
//	}  
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}