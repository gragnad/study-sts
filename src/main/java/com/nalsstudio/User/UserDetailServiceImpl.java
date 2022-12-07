package com.nalsstudio.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nalsstudio.domain.User;

@Service
public class UserDetailServiceImpl implements UserDetailService{
	
	private UserRepository userRepo;
	
	public UserDetailServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUserName(userName);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UsernameNotFoundException("User : " + userName + " Not Found");
	}

}
