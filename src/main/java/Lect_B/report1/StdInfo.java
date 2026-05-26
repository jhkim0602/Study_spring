package Lect_B.report1;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StdInfo {

	private String stdName;
	private String stdNum;

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getStdNum() {
		return stdNum;
	}

	public void setStdNum(String stdNum) {
		this.stdNum = stdNum;
	}

	@Override
	public String toString() {
		return "성명: " + stdName + ", 학번: " + stdNum;
	}
}
