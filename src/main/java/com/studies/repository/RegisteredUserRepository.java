package com.studies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studies.model.RegisteredUser;

@Repository("registeredUserRepository")
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
	RegisteredUser findByUsername(String username);
}

