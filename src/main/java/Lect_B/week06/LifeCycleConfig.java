package Lect_B.week06;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifeCycleConfig {

	@Bean(initMethod = "init", destroyMethod = "cleanup")
	public InitDestroyUnit myBean() {
		return new InitDestroyUnit();
	}
}
