package com.david.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.david.cursomc.services.DBService;
import com.david.cursomc.services.EmailService;
import com.david.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value(value = "spring.jpa.hibernate.ddl-auto")
	private String Strategy;
			
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		if(!"create".equals(Strategy))
			return false;
		
		dbService.instantiateTestDataBase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	

}
