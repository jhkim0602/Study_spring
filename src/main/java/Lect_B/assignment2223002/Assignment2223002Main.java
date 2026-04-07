package Lect_B.assignment2223002;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Assignment2223002Main {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(Assignment2223002Config.class)) {
			StdManager stdManager = context.getBean("stdManager", StdManager.class);
			stdManager.inputStudents();
		}
	}
}
