package Lect_B.week05;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

public interface Animal {

	String getName();

	String sound();

	default String getSound() {
		return sound();
	}
}

@Component("dog")
@Order(1)
class Dog implements Animal {

	@Override
	public String getName() {
		return "Dog";
	}

	@Override
	public String sound() {
		return "멍멍";
	}

	@Override
	public String toString() {
		return getName() + "(" + sound() + ")";
	}
}

@Component("cat")
@Order(2)
class Cat implements Animal {

	@Override
	public String getName() {
		return "Cat";
	}

	@Override
	public String sound() {
		return "야옹";
	}

	@Override
	public String toString() {
		return getName() + "(" + sound() + ")";
	}
}
