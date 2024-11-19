package com.maveric.bank.JWT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maveric.bank.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service	
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepository;
	
	private com.maveric.bank.entity.User userDetails;
	
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        log.info("Inside loadUserByUsername for username: {}", username);

	        // Fetch user from repository using email/username
	        userDetails = userRepository.findByEmail(username);

	        if (Objects.isNull(userDetails)) {
	            throw new UsernameNotFoundException("User not found: " + username);
	        }

	        // Map roles from user entity to GrantedAuthority collection
	        Collection<GrantedAuthority> authorities = mapRolesToAuthorities(userDetails.getRoles());

	        return new User(userDetails.getEmail(), userDetails.getPassword(), authorities);
	    }
	
	
	public com.maveric.bank.entity.User getuserDetails()
	{
		return userDetails;

    }
	private Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
    }
	

}
