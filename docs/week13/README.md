# Week 13

## 주제

13주차는 **커맨드 객체 검증 · 메시지 태그 · 타입 변환**을 배우는 주차다.

11주차에서 컨트롤러가 폼 데이터를 자바 빈 객체(커맨드 객체)로 자동 바인딩하는 흐름을 봤다면,
13주차는 그 커맨드 객체의 값이 **올바른지 검증**하는 방법과,
사용자 입력 문자열을 **다른 타입으로 변환**하는 방법을 다룬다.

핵심 파일:

- `RegisterRequestForm`
- `RegisterRequestValidator`
- `FormatCommand`
- `MvcConfig2`
- `BasicProcessController2`
- `messages.properties` (11주차에서 이미 작성됨, 검증 메시지 키 재사용)

## 이 주차가 왜 중요한가

폼 입력 값을 검증하지 않으면 잘못된 값이 그대로 DB에 저장된다.
에러 메시지를 제대로 보여주지 않으면 사용자가 어디를 고쳐야 할지 모른다.

스프링 MVC는 검증을 위해 다음 세 가지 접근을 모두 제공한다.

- 인터페이스 구현 (`Validator`) → 수동
- JSR-380 어노테이션 (`@Email`, `@Size`, `@Pattern` 등) → 선언적
- 글로벌 `Validator` (`@Validated`) → 모든 컨트롤러 일괄 적용

추가로, 타입 변환을 위한 `@DateTimeFormat`, `@NumberFormat` 어노테이션도 다룬다.

즉 13주차의 역할은:

- 강의자료의 Ex1~Ex5 흐름을 따라가며
- 같은 폼을 4가지 검증 방식(검증 없음 / 수동 Validator / @Valid / 글로벌 Validator)으로 비교하고
- 폼 데이터의 타입 변환을 `@DateTimeFormat`, `@NumberFormat` 으로 처리하는 것

이다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [11주차](../week11/README.md)의 컨트롤러/커맨드 객체/`MessageSource` 흐름

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 이 주차에서 꼭 잡아야 하는 질문

- `<spring:message>` 태그는 무엇을 하는가? 하드코딩 대비 어떤 장점이 있는가?
- `<form:form>`, `<form:input>`, `<form:errors>` 같은 스프링 폼 태그의 역할은?
- `Validator` 인터페이스의 `supports()` 와 `validate()` 는 각각 무엇을 하는가?
- `Errors` / `BindingResult` 는 어떻게 다르고 어디에 쓰는가?
- `rejectValue(field, code)` 는 메시지 코드를 어떤 순서로 검색하는가?
- `@Valid` 와 `@Validated` 는 어떻게 다른가?
- JSR-380 어노테이션(`@NotNull`, `@Email`, `@Size`, `@Pattern` 등)을 쓰면 무엇이 줄어드는가?
- `WebMvcConfigurer.getValidator()` 로 글로벌 Validator 를 설정하면 어떤 효과가 있는가?
- `@DateTimeFormat`, `@NumberFormat` 은 언제 적용되는가?

## 추천 읽기 순서

1. `theory.md`에서 검증 인터페이스(Validator/Errors/BindingResult) 와 JSR-380 어노테이션 개요 확인
2. `RegisterRequestForm`, `FormatCommand` 코드 확인 (커맨드 객체와 어노테이션)
3. `RegisterRequestValidator` 의 `supports()` / `validate()` 구현 확인
4. `BasicProcessController2` 의 Ex1~Ex5 메서드를 순서대로 읽기
5. `MvcConfig2` 의 `getValidator()` 주석 처리 의도 이해
6. `views/week13/registerForm.jsp` 의 `<form:form>`, `<form:errors>` 태그 확인
7. `/week13` 라우트 5개를 직접 호출하면서 각 검증 흐름 결과 확인
8. `practice.md` 로 각 라우트의 결과 매핑 확인

## 빠른 요약

- 13주차는 커맨드 객체 검증의 3가지 접근법(수동 / `@Valid` / 글로벌) 과 타입 변환을 한 번에 정리하는 주차다.
- `<spring:message>` + `<form:form>` 으로 메시지/폼 출력을 단순화한다.
- `Validator` 인터페이스는 `supports()` 로 검증 대상을 가리고, `validate()` 에서 `Errors` 에 오류를 등록한다.
- JSR-380 어노테이션은 커맨드 객체 필드에 직접 붙여 선언적 검증을 한다.
- 글로벌 `Validator` 는 `WebMvcConfigurer.getValidator()` 로 등록되며, `@Validated` 어노테이션과 함께 사용한다.
- `@DateTimeFormat`, `@NumberFormat` 은 요청 파라미터 문자열을 `LocalDateTime` / `Double` 등으로 자동 변환한다.

## 이전 주차 연결

- [11주차 문서](../week11/README.md)
  - 11주차의 커맨드 객체 자동 바인딩이 13주차 검증의 출발점이다.
  - 11주차의 `MessageSource` 와 `messages.properties` 가 13주차 검증 메시지 표시에 그대로 쓰인다.
