package Lect_B.assignment2223002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:static/assignment2223002.properties")
public class Assignment2223002Config {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public StdInfo stdInfo() {
		return new StdInfo();
	}

	@Bean
	public List<StdInfo> stdInfoList() {
		return new ArrayList<>();
	}

	@Bean(destroyMethod = "printResult")
	public StdManager stdManager(List<StdInfo> stdInfoList,
			@Value("${assignment2223002.student-count}") int studentCount) {
		StdManager stdManager = new StdManager();
		stdManager.setStdInfoList(stdInfoList);
		stdManager.setStudentCount(studentCount);
		return stdManager;
	}
}
