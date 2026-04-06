package Lect_B.week05;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class Week05LifecycleBean implements InitializingBean, DisposableBean {

	private final List<String> events = new ArrayList<>();

	public Week05LifecycleBean() {
		events.add("1. constructor");
	}

	@PostConstruct
	public void postConstruct() {
		events.add("2. @PostConstruct");
	}

	@Override
	public void afterPropertiesSet() {
		events.add("3. afterPropertiesSet()");
	}

	public void customInit() {
		events.add("4. custom init-method");
	}

	@PreDestroy
	public void preDestroy() {
		events.add("5. @PreDestroy");
	}

	@Override
	public void destroy() {
		events.add("6. DisposableBean.destroy()");
	}

	public void customDestroy() {
		events.add("7. custom destroy-method");
	}

	public List<String> snapshotEvents() {
		return new ArrayList<>(events);
	}
}
