package Lect_B.week11;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {

	private final String title;
	private List<String> options;

	public boolean isChoice() {
		return options != null && !options.isEmpty();
	}
}
