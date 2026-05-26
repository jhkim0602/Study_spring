package Lect_B.week13;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 13주.Ex4 : 글로벌 범위 Validator 설정 위치 (원본 강의자료 그대로 주석 처리)
//
// 학습 순서
// 1) Ex3 (@Valid 어노테이션 검증)을 먼저 확인 - 이 상태에서는 JSR-380 어노테이션이 동작
// 2) 아래 getValidator() 주석을 풀면 @Validated 가 RegisterRequestValidator 를 사용
//    단, 동시에 @Valid 도 같은 Validator 로 바뀌므로 JSR-380 동작은 가려진다.
//
// 따라서 이 파일은 의도적으로 비활성화 상태로 둔다. 학생은 Ex4 학습 시점에만 주석을 푼다.
@Configuration
public class MvcConfig2 implements WebMvcConfigurer {

	/*
	// 13주.Ex4 글로벌 Validator
	@Override
	public org.springframework.validation.Validator getValidator() {
		return new RegisterRequestValidator();
	}
	*/
}
