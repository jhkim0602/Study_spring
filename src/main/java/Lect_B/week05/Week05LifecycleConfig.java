package Lect_B.week05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Week05LifecycleConfig {

	@Bean(initMethod = "customInit", destroyMethod = "customDestroy")
	public Week05LifecycleBean week05LifecycleBean() {
		return new Week05LifecycleBean();
	}
}
