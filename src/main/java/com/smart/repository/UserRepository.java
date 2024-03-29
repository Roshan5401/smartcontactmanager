package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByEmail(String username);

	

}
