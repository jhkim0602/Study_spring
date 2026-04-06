package Lect_B.week06;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class InitDestroyUnit implements InitializingBean, DisposableBean {

	private final List<String> events = new ArrayList<>();

	public InitDestroyUnit() {
		events.add("1. constructor");
	}

	public void init() {
		events.add("4. init()");
	}

	public void cleanup() {
		events.add("7. cleanup()");
	}

	@PostConstruct
	public void postConstruct() {
		events.add("2. @PostConstruct");
	}

	@Override
	public void afterPropertiesSet() {
		events.add("3. afterPropertiesSet()");
	}

	@PreDestroy
	public void preDestroy() {
		events.add("5. @PreDestroy");
	}

	@Override
	public void destroy() {
		events.add("6. destroy()");
	}

	public List<String> snapshotEvents() {
		return new ArrayList<>(events);
	}
}
