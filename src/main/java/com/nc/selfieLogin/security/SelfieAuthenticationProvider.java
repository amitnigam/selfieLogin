package com.nc.selfieLogin.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;



public class SelfieAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{

	
	public SelfieAuthenticationProvider(SelfieUserDetailService selfieUserDetailService) {
		
		this.selfieUserDetailService = selfieUserDetailService;
	}

	private SelfieUserDetailService selfieUserDetailService ;
	
	public SelfieUserDetailService getSelfieUserDetailService() {
		return selfieUserDetailService;
	}

	public void setSelfieUserDetailService(SelfieUserDetailService selfieUserDetailService) {
		this.selfieUserDetailService = selfieUserDetailService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
		
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication ;
UserDetails loadedUser;

loadedUser = selfieUserDetailService.loadUserBySelfie(username, auth.getCredentials()) ;

if (loadedUser == null) {
    throw new BadCredentialsException("UserDetailsService returned null, "
        + "user couldn't be found");
}
		return loadedUser;
	}

}
