package Lect_B.week06;

import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AwareInterfaceImp implements BeanNameAware, ApplicationContextAware {

	private ApplicationContext context;
	private String beanName;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public String getBeanName() {
		return beanName;
	}

	public String[] getSortedBeanNames() {
		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		return beanNames;
	}
}
