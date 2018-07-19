package com.nc.selfieLogin.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SelfieUserDetailService {

	 UserDetails loadUserBySelfie(String username, Object selfie) throws UsernameNotFoundException;
	 
	 /* Accepts face id got from detect API, and returns face id that matched with it. if none matched, will return null */
	 String findFaceMatch(String detectedFaceId) ;
	 
	 
}
