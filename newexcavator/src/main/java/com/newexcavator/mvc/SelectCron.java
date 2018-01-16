package com.newexcavator.mvc;

import org.springframework.beans.factory.annotation.Autowired;

import com.newexcavator.service.UserService;

public class SelectCron {
	@Autowired
	private UserService userService;
	
	public void execute() {
		userService.queryOne();
	}
}
