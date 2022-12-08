package com.nalsstudio.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nalsstudio.domain.Authorities;
import com.nalsstudio.domain.User;
import com.nalsstudio.dto.UserDto;

@Service
public class UserDetailServiceImpl implements UserDetailService{
	
	private UserRepository userRepo;
	private AuthoritiesRepository AuthRepo;
	
	public UserDetailServiceImpl(UserRepository userRepo, AuthoritiesRepository authRepo) {
		this.userRepo = userRepo;
		AuthRepo = authRepo;
	}

	@Override
	public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(userName);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UsernameNotFoundException("User : " + userName + " Not Found");
	}
	
	@Override
	public User loginUser(String name, String password){
		Optional<User> user = userRepo.findByUsernameAndPassword(name, password);
		if(user.isPresent()) {
			return user.get();
		}
		return null;
	}
	
	@Override
	public Authorities registerRole(User user, String role) {
		Optional<User> findUser = userRepo.findByUsername(user.getUsername());
		if(!findUser.isPresent()) {
			User inUser = userRepo.save(user);
			Authorities authority = new Authorities(user.getUsername(), role);
			Authorities inAuthority = AuthRepo.save(authority);
			if(inUser != null && inAuthority != null) {
				return authority;
			}	
		}
		return null;
	}

}
