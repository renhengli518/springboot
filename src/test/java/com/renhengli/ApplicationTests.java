package com.renhengli;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.renhengli.entity.User;
import com.renhengli.rabbit.Receiver;
import com.renhengli.rabbit.Sender;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Autowired
	@Qualifier("mailSender") 
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private Sender sender;
	
	@Autowired
	private Receiver receiver;

	@Test
	public void test() throws Exception {

		// 保存字符串
		stringRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

		// 保存对象
		User user = new User(1l, "超人", 20);
		redisTemplate.opsForValue().set(user.getName(), user);

		user = new User(2l, "蝙蝠侠", 30);
		redisTemplate.opsForValue().set(user.getName(), user);

		user = new User(3l, "蜘蛛侠", 40);
		redisTemplate.opsForValue().set(user.getName(), user);

		Assert.assertEquals(20, redisTemplate.opsForValue().get("超人").getAge().longValue());
		Assert.assertEquals(30, redisTemplate.opsForValue().get("蝙蝠侠").getAge().longValue());
		Assert.assertEquals(40, redisTemplate.opsForValue().get("蜘蛛侠").getAge().longValue());

	}

	@Test
	public void test1() throws Exception {
		stringRedisTemplate.opsForValue().set("aaa", "111");
		Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
	}

	@Test
	public void testObj() throws Exception {
		User user = new User(1l, "aa123456", 18);
		ValueOperations<String, User> operations = redisTemplate.opsForValue();
		operations.set("com.neox", user);
		operations.set("com.neo.f", user, 1, TimeUnit.SECONDS);
		Thread.sleep(1000);
		// redisTemplate.delete("com.neo.f");
		boolean exists = redisTemplate.hasKey("com.neo.f");
		if (exists) {
			System.out.println("exists is true");
		} else {
			System.out.println("exists is false");
		}
		// Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
	}

	/**
	 * 测试发邮件
	 * 
	 * @throws Exception
	 */
	@Test
	public void sendSimpleMail() throws Exception {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("985602166@qq.com");
		message.setTo("rhl@linkgap.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");

		mailSender.send(message);
	}

	@Test
	public void sendAttachmentsMail() throws Exception {

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
	}

	@Test
	public void sendInlineMail() throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("985602166@qq.com");
		helper.setTo("rhl@linkgap.com");
		helper.setSubject("主题：嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true);

		FileSystemResource file = new FileSystemResource(new File("E:/temp/image/test.jpg"));
		helper.addInline("weixin", file);

		mailSender.send(mimeMessage);
	}

	@Test
	public void sendTemplateMail() throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("985602166@qq.com");
		helper.setTo("rhl@linkgap.com");
		helper.setSubject("主题：模板邮件");

		Map<String, Object> model = new HashMap<String,Object>();
		model.put("username", "didi");
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "template.vm", "UTF-8", model);
		helper.setText(text, true);

		mailSender.send(mimeMessage);
	}

	
	

	/**
	 * rabbitMQ test
	 * @throws Exception
	 */
	@Test
	public void send() throws Exception {
		sender.send();;
	}
	
	@Test
	public void receiver() throws Exception {
		receiver.process("hello");
	}
	
}