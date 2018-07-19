package com.nc.selfieLogin.service;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nc.common.service.StorageService;
import com.nc.selfieLogin.dao.repository.UserRepository;




@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private StorageService storageService ;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("VISITOR");     
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
       // String userImage = storageService.store(user.getMainimage(), "mainImage") ;
        String faceId = storageService.storeVisitorInFaceList(user.getMainimage()) ;
        user.setHeadShot(faceId);
		userRepository.save(user);
	}

}