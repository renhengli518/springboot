package com.renhengli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.renhengli.entity.User;
import com.renhengli.mapper.UserMapper;

/**
 * 
 * @author renhengli
 *
 */
@RestController
@RequestMapping({ "/home" })
public class UserController {
	@Autowired
	UserMapper userMapper;

	@RequestMapping(value = "/user")
	@ResponseBody
	public String user(ModelAndView md) {
		User user = userMapper.findUserByName("王伟");
		String ss = user.getName() + "-----" + user.getAge();
		return ss;
	}

}
