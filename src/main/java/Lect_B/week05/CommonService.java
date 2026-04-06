package Lect_B.week05;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class CommonService extends AbstractCommonService {

	private int periodTime;

	public String summarize() {
		return "animal=%s, defaultValue=%d, periodTime=%d".formatted(
				getCat().getName(), getDefaultValue(), periodTime);
	}
}
