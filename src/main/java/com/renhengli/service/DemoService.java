package com.renhengli.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.renhengli.entity.User;

@Service  
public class DemoService {  
//    @Cacheable(value = "usercache",keyGenerator = "wiselyKeyGenerator")  
//    public User findUser(Long id,String name,int age){  
//        System.out.println("无缓存的时候调用这里");  
//        return new User(id,name,age);  
//    }  
	
}  