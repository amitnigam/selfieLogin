package com.nc.selfieLogin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



public class SelfieAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	public static final String SPRING_SECURITY_FORM_SELFIE_KEY = "mainimage";
	private final static Logger log = LogManager.getLogger() ;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " 
              + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String email = obtainUsername(request);
       // String password = obtainPassword(request);
        Object selfieImage = obtainSelfie(request);

        if (email == null) {
            email = "";
        }
       

        return new UsernamePasswordAuthenticationToken(email, selfieImage);
    }

    private Object obtainSelfie(HttpServletRequest request) {
        try {
        	log.info("Fetching selfie image from the form");
			return request.getPart(SPRING_SECURITY_FORM_SELFIE_KEY).getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	
}
