package com.studies.service;

import com.studies.model.RegisteredUser;

public interface RegisteredUserService {
	public RegisteredUser findUserByUsername(String username);
	public void  saveUser(RegisteredUser user);
}
