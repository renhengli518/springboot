package com.renhengli;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 2017-1-11
 * 
 * @author renhengli
 *
 */
@SpringBootApplication
@EnableScheduling
public class Application {
	private static Logger logger = Logger.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		logger.info("---------server start success !------------");
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return (new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				//读取static下面的错误页面
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/static/401.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/static/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/static/500.html");
				container.addErrorPages(error401Page, error404Page, error500Page);
			}
		});
	}
}