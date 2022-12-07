package com.nalsstudio.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nalsstudio.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
	Optional<User> findByUserName(String userName);
}
