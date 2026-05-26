package Lect_B.report1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
@PropertySource(value = "classpath:static/student.properties", encoding = "UTF-8")
public class StdManager implements ApplicationContextAware, InitializingBean {

	@Value("${stdCnt}")
	private int stdCnt;

	@Value("${names}")
	private String names;

	@Value("${numbers}")
	private String numbers;

	private ApplicationContext context;
	private final List<StdInfo> stdList = new ArrayList<>();

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void afterPropertiesSet() {
		String[] nameArray = names.trim().split("\\s+");
		String[] numberArray = numbers.trim().split("\\s+");

		int inputCount = Math.min(stdCnt, Math.min(nameArray.length, numberArray.length));
		for (int i = 0; i < inputCount; i++) {
			StdInfo stdInfo = context.getBean(StdInfo.class);
			stdInfo.setStdName(nameArray[i]);
			stdInfo.setStdNum(numberArray[i]);
			stdList.add(stdInfo);
		}
	}

	public List<StdInfo> getStdList() {
		return Collections.unmodifiableList(stdList);
	}

	@PreDestroy
	public void printResult() {
		System.out.println("===== 학생 정보 출력 =====");
		for (StdInfo stdInfo : stdList) {
			System.out.println(stdInfo);
		}
		System.out.println("총 학생 수: " + stdList.size());
	}
}
