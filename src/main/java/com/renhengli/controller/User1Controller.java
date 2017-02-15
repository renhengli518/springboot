package com.renhengli.controller;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.renhengli.entity.User;
import com.renhengli.exception.MyException;
import com.renhengli.mapper.UserMapper;
import com.renhengli.service.DemoService;

/**
 * 
 * @author renhengli
 *
 */
@Controller
@RequestMapping("/home")
public class User1Controller {
	private static Logger logger = Logger.getLogger(User1Controller.class);

	@Autowired
	UserMapper userMapper;

	@Autowired
	DemoService demoService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Autowired
	@Qualifier("mailSender")
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	// 从 application.properties 中读取配置，如取不到默认值为Hello Shanhy
	@Value("${application.hello:Hello Angel}")
	private String hello;

	@RequestMapping("/user1")
	public String user1(Map<String, Object> map) {
		logger.info("-----start-------");
		logger.debug("-----debug log-------");
		logger.error("-----error log-------");
		System.out.println("UserController.user1().hello=" + hello);
		map.put("hello", hello);
		logger.debug("-----end---------");
		logger.info("-----end---------");
		return "user";
	}

	@RequestMapping("/test")
	@ResponseBody
	public String putCache() throws MyException {
		try {
			// demoService.findUser(1l, "wang", 20);
			System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e.getMessage());
		}
	}

	@RequestMapping("/test2")
	@ResponseBody
	public String testCache() {
		// User user = demoService.findUser(1l, "wang", 22);
		System.out.println("我这里没执行查询");
		// System.out.println("user:" + "/" + user.getName() + "/" +
		// user.getAge());
		return "ok";
	}

	/**
	 * 测试自定义异常
	 * 
	 * @return
	 * @throws MyException
	 */
	@RequestMapping("/json")
	public String json() throws MyException {
		throw new MyException("发生错误2");
	}

	@RequestMapping("/hello")
	public String hello() throws Exception {
		throw new Exception("发生错误");
	}

	@RequestMapping("/redis")
	@ResponseBody
	public String redis() throws Exception {
		// 保存字符串
		stringRedisTemplate.opsForValue().set("aaa", "111");
		System.out.println("------------" + stringRedisTemplate.opsForValue().get("aaa") + "--------------------");
		// 保存对象
		User user = new User(1l, "超人", 20);
		redisTemplate.opsForValue().set(user.getName(), user, 50, TimeUnit.SECONDS);
		System.out.println("--------------" + redisTemplate.getExpire("超人"));

		user = new User(2l, "蝙蝠侠", 30);
		redisTemplate.opsForValue().set(user.getName(), user);

		user = new User(3l, "蜘蛛侠", 40);
		redisTemplate.opsForValue().set(user.getName(), user);
		return "ok";
	}

	@RequestMapping("/freemarker")
	public String index(ModelMap map) {
		map.addAttribute("host", "http://blog.didispace.com");
		return "index";
	}

	@RequestMapping("/sendMail1")
	public String sendMail1(ModelMap map) throws MyException {
		try {
			map.addAttribute("host", "http://blog.didispace.com");
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("985602166@qq.com");
			message.setTo("rhl@linkgap.com");
			message.setSubject("主题：简单邮件");
			message.setText("测试邮件内容");

			mailSender.send(message);
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "index";
		}
	}

	@RequestMapping("/sendMail2")
	public String sendMail2(ModelMap map, HttpServletRequest request) throws MyException {
		try {
			map.addAttribute("host", "http://blog.didispace.com");
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom("985602166@qq.com");
			helper.setTo("rhl@linkgap.com");
			helper.setSubject("主题：有附件");
			helper.setText("有附件的邮件");

			FileSystemResource file = new FileSystemResource(new File("E:/temp/image/test.jpg"));
			helper.addAttachment("附件-1.jpg", file);
			helper.addAttachment("附件-2.jpg", file);

			mailSender.send(mimeMessage);
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "index";
		}
	}

}
