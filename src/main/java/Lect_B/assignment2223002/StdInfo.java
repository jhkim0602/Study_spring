package Lect_B.assignment2223002;

// 학생 1명의 이름과 학번 정보를 저장하는 클래스
public class StdInfo {

	// 학생 이름을 저장하는 필드
	private String studentName;

	// 학생 학번을 저장하는 필드
	private String studentId;

	// 저장된 학생 이름을 반환
	public String getStudentName() {
		return studentName;
	}

	// 입력받은 학생 이름을 저장
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	// 저장된 학생 학번을 반환
	public String getStudentId() {
		return studentId;
	}

	// 입력받은 학생 학번을 저장
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	// 결과 출력 시 보기 좋게 문자열 형태로 변환
	public String toString() {
		return "이름: " + studentName + ", 학번: " + studentId;
	}
}
