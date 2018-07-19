package com.nc.selfieLogin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nc.selfieLogin.CustomAuthenticationFailureHandler;
import com.nc.selfieLogin.security.SelfieAuthenticationFilter;
import com.nc.selfieLogin.security.SelfieAuthenticationProvider;
import com.nc.selfieLogin.security.SelfieUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Autowired
	private SelfieUserDetailService selfieUserDetailService ;

	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
			
		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		//.antMatchers("/**").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/registration").permitAll()
		.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
		.authenticated().and().csrf().disable().formLogin()		
		.loginPage("/login")
		.and().logout();
	}
	public SelfieAuthenticationFilter authenticationFilter() throws Exception {
		SelfieAuthenticationFilter selfieAuthFilter = new SelfieAuthenticationFilter() ;
		selfieAuthFilter.setAuthenticationManager(authenticationManagerBean());
		selfieAuthFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
		selfieAuthFilter.setAuthenticationSuccessHandler(successHandler());
		return selfieAuthFilter ;
		
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }
	
	public AuthenticationProvider authProvider(){
		SelfieAuthenticationProvider provider =  new SelfieAuthenticationProvider(selfieUserDetailService) ;
		return provider ;
	}
	
	public CustomAuthenticationFailureHandler failureHandler() {
       		return new CustomAuthenticationFailureHandler();
    }
	
	public AuthenticationSuccessHandler successHandler(){
		
		AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler() {	
		private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy() ;

		@Override
		public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
				Authentication authentication) throws IOException, ServletException {
			redirectStrategy.sendRedirect(request, response, "/welcome") ;
			
		};
		} ;
		
		return successHandler ;
		
	}
	
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}