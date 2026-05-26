package Lect_B.week13;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import lombok.Getter;
import lombok.Setter;

// 13주.Ex5 : 커맨드 객체에 @DateTimeFormat / @NumberFormat 을 적용해 타입 변환
@Getter
@Setter
public class FormatCommand {

	// "2026-05-18 15:39:14" → LocalDateTime 자동 변환
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateTime;

	// LocalDateTime 을 Date 로 변환한 결과 (JSTL <fmt:formatDate> 사용용)
	private Date date;

	@NumberFormat(pattern = "#,###.##")
	private Double number;

	@NumberFormat(style = NumberFormat.Style.CURRENCY)
	private Double price;

	@NumberFormat(style = NumberFormat.Style.PERCENT)
	private Double percentage;
}
