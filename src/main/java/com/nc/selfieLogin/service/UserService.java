package com.nc.selfieLogin.service;

import com.nc.selfieLogin.dao.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}