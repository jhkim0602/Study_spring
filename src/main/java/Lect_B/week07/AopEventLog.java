package Lect_B.week07;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AopEventLog {

	private final ThreadLocal<List<String>> events = ThreadLocal.withInitial(ArrayList::new);

	public void reset() {
		events.get().clear();
	}

	public void add(String event) {
		events.get().add(event);
	}

	public List<String> snapshot() {
		return List.copyOf(events.get());
	}
}
