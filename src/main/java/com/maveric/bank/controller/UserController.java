package com.maveric.bank.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.bank.entity.User;
import com.maveric.bank.service.IUserService;



@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> SignUp(@RequestBody(required = true)Map<String, String> requestMap)
	{
		try {
			return userService.signUp(requestMap);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity("message:Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody(required = true)Map<String, String> requestMap)
	{
		try {
			return userService.login(requestMap);		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity("message:Something went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
