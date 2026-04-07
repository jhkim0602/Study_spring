package Lect_B.assignment2223002;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class StdManager implements ApplicationContextAware, InitializingBean {

	private List<StdInfo> stdInfoList;
	private int studentCount;
	private ApplicationContext applicationContext;

	public void setStdInfoList(List<StdInfo> stdInfoList) {
		this.stdInfoList = stdInfoList;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		System.out.println("[초기화] StdManager 준비 완료");
		System.out.println("[초기화] 입력할 학생 수: " + studentCount);
	}

	public void inputStudents() {
		try (Scanner scanner = new Scanner(System.in)) {
			for (int i = 0; i < studentCount; i++) {
				StdInfo stdInfo = applicationContext.getBean("stdInfo", StdInfo.class);

				System.out.print((i + 1) + "번째 학생 이름 입력: ");
				stdInfo.setStudentName(scanner.nextLine());

				System.out.print((i + 1) + "번째 학생 학번 입력: ");
				stdInfo.setStudentId(scanner.nextLine());

				stdInfoList.add(stdInfo);
				System.out.println("학생 정보가 리스트에 저장되었습니다.");
				System.out.println();
			}
		}
	}

	public void printResult() {
		System.out.println();
		System.out.println("===== 저장된 학생 정보 출력 =====");
		for (int i = 0; i < stdInfoList.size(); i++) {
			System.out.println((i + 1) + ". " + stdInfoList.get(i));
		}
		System.out.println("총 학생 수: " + stdInfoList.size());
	}
}
