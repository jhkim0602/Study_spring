package Lect_B.week05;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Week05AwareComponent implements BeanNameAware, ApplicationContextAware {

	private String beanName;
	private ApplicationContext applicationContext;

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String getBeanName() {
		return beanName;
	}

	public int getBeanDefinitionCount() {
		return applicationContext.getBeanDefinitionCount();
	}

	public boolean isWorkUnitPrototype() {
		return applicationContext.isPrototype("workUnit");
	}

	public String lookupSmsSender() {
		SmsSender sender = applicationContext.getBean("smsSender", SmsSender.class);
		return sender.getSenderName() + " / instanceId=" + sender.getInstanceId();
	}
}
