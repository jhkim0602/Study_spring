package Lect_B.week03;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public SmsSender configSms() {
		return new SmsSender("학교실습용 SMS 발신기");
	}
}
