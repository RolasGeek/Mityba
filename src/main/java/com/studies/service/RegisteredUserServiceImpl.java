package com.studies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.studies.model.RegisteredUser;
import com.studies.repository.RegisteredUserRepository;


@Service("registeredUserService")
public class RegisteredUserServiceImpl implements RegisteredUserService {

	@Autowired
	private RegisteredUserRepository userRep;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	public RegisteredUser findUserByUsername(String username) {
		return userRep.findByUsername(username);
	}

	@Override
	public void saveUser(RegisteredUser user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setActive(1);
		user.setUserLevel(1);
		userRep.save(user);
		
	}

}
