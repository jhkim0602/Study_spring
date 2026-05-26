# Week 13 Theory

## 주제

13주차는 **커맨드 객체 검증과 타입 변환**을 다룬다.

11주차에서 컨트롤러 메서드가 폼 데이터를 자바 빈 객체로 자동 바인딩하는 흐름까지 봤다.
13주차는 그 객체의 **값이 올바른지 검증**하고, 입력 문자열을 **다른 타입으로 변환**하는 단계로 넘어간다.

## 이 문서를 읽기 전에

13주차를 이해하려면 다음 흐름을 기억해야 한다.

- 11주차: 컨트롤러 메서드는 폼 데이터를 커맨드 객체(자바 빈)로 자동 바인딩 받는다
- 11주차: `MessageSource` + `messages.properties` 로 다국화 메시지를 관리한다

13주차는 이 흐름 위에서 다음 질문으로 넘어간다.

> "커맨드 객체로 들어온 값이 잘못됐으면 어떻게 거부하고, 사용자에게 어떻게 알리는가?"
> "요청 파라미터 문자열을 LocalDateTime/Double 같은 다른 타입으로 어떻게 자동 변환하는가?"

## 현재 프로젝트에서 먼저 볼 코드

- [`RegisterRequestForm.java`](../../src/main/java/Lect_B/week13/RegisterRequestForm.java)
- [`RegisterRequestValidator.java`](../../src/main/java/Lect_B/week13/RegisterRequestValidator.java)
- [`FormatCommand.java`](../../src/main/java/Lect_B/week13/FormatCommand.java)
- [`MvcConfig2.java`](../../src/main/java/Lect_B/week13/MvcConfig2.java)
- [`BasicProcessController2.java`](../../src/main/java/Lect_B/week13/BasicProcessController2.java)
- [`Week13IndexController.java`](../../src/main/java/Lect_B/week13/Week13IndexController.java)
- [`messages.properties`](../../src/main/resources/messages.properties)
- [`src/main/webapp/views/week13`](../../src/main/webapp/views/week13)

## 목차

- [1. 13주차의 핵심 질문](#1-13주차의-핵심-질문)
- [2. <spring:message> 태그](#2-springmessage-태그)
- [3. <form:form>과 스프링 폼 태그](#3-formform과-스프링-폼-태그)
- [4. 검증 인터페이스 개요](#4-검증-인터페이스-개요)
- [5. Validator 인터페이스 구현](#5-validator-인터페이스-구현)
- [6. ValidationUtils와 rejectValue](#6-validationutils와-rejectvalue)
- [7. 에러 코드 검색 규칙](#7-에러-코드-검색-규칙)
- [8. <form:errors>로 오류 출력](#8-formerrors로-오류-출력)
- [9. JSR-380 어노테이션과 @Valid](#9-jsr-380-어노테이션과-valid)
- [10. 글로벌 Validator와 @Validated](#10-글로벌-validator와-validated)
- [11. @DateTimeFormat / @NumberFormat](#11-datetimeformat--numberformat)
- [12. 현재 프로젝트에서 구현한 실습 구조](#12-현재-프로젝트에서-구현한-실습-구조)
- [13. 자주 헷갈리는 질문](#13-자주-헷갈리는-질문)
- [14. 시험 대비 핵심 정리](#14-시험-대비-핵심-정리)

## 1. 13주차의 핵심 질문

13주차를 한 문장으로 요약하면:

> "커맨드 객체 검증의 세 가지 방식(수동 Validator / @Valid / 글로벌 Validator)을 구분하고, 타입 변환을 어노테이션으로 처리한다"

이다.

## 2. <spring:message> 태그

### 왜 필요한가

JSP 안에 문자열을 그대로 박아 두면 다음 문제가 생긴다.

- 동일한 문자열을 여러 곳에서 사용 → 한 번 바꾸려면 전부 찾아 고쳐야 함
- 다국어 지원 불가 → 사용자의 언어 설정에 따라 보여줄 수 없음

해결: 문자열을 `messages.properties` 같은 외부 파일에 두고, JSP에서는 키만 참조한다.

### 사용 방법

```jsp
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="member.info" />
```

`<spring:message>` 태그는 내부적으로 `MessageSource.getMessage("member.info", null, locale)` 을 호출해
현재 로케일에 맞는 문자열로 치환한다.

### 메시지 인자 처리

`messages.properties`:

```properties
register.done=<strong>{0}/({1})</strong>님 회원 가입을 완료했습니다.
```

JSP:

```jsp
<spring:message code="register.done">
	<spring:argument value="${registerRequest.name}" />
	<spring:argument value="${registerRequest.email}" />
</spring:message>
```

`{0}`, `{1}` 자리에 `<spring:argument>` 값이 인덱스 순서대로 대치된다.

## 3. <form:form>과 스프링 폼 태그

스프링이 제공하는 폼 태그를 쓰면 커맨드 객체의 값 출력이 단순해진다.

### 선언

```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
```

### 주요 태그

| 태그 | HTML 매핑 | 설명 |
|---|---|---|
| `<form:form>` | `<form>` | `modelAttribute` 로 커맨드 객체 이름 지정 |
| `<form:input path="email">` | `<input type="text">` | 자바 빈의 `email` 필드와 연결, 현재 값 자동 출력 |
| `<form:password path="password">` | `<input type="password">` | password 필드 매핑 |
| `<form:checkbox path="agree">` | `<input type="checkbox">` | boolean 필드 |
| `<form:errors path="email">` | `<span>` | 해당 필드의 오류 메시지 출력 |

### 핵심 속성

| 속성 | 설명 |
|---|---|
| `modelAttribute` | 폼 데이터와 매핑할 커맨드 객체 이름 |
| `action` | 폼 제출 URL |
| `method` | HTTP 메서드 (`get` / `post`) |
| `path` (내부 태그) | 커맨드 객체의 필드 이름 |

현재 프로젝트의 `registerForm.jsp` 가 이 패턴을 그대로 보여 준다.

## 4. 검증 인터페이스 개요

스프링 MVC 는 검증을 위해 다음 세 가지를 제공한다.

| 인터페이스 | 역할 |
|---|---|
| `org.springframework.validation.Validator` | 객체 검증 인터페이스 |
| `org.springframework.validation.Errors` | 검증 결과(오류 코드/필드) 저장 |
| `org.springframework.validation.BindingResult` | `Errors` 상속, 검증 대상 객체 정보 추가 |

기억할 흐름:

```text
컨트롤러 메서드
  -> Validator.validate(target, errors)
      -> errors.rejectValue("email", "email.invalid")  // 필드 오류 등록
  -> result.hasErrors() ? 폼 다시 보여주기 : 다음 단계
```

## 5. Validator 인터페이스 구현

```java
public interface Validator {
    boolean supports(Class<?> clazz);
    void validate(Object target, Errors errors);
}
```

- `supports(clazz)` : 이 Validator 가 검증할 수 있는 타입인지 확인
- `validate(target, errors)` : 실제 검증 로직, 오류를 `errors` 에 등록

### 현재 프로젝트의 `RegisterRequestValidator`

```java
public class RegisterRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequestForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequestForm regReq = (RegisterRequestForm) target;
        if (regReq.getEmail() == null || !regReq.getEmail().contains("@")) {
            errors.rejectValue("email", "email.invalid");
        }
        // ...
    }
}
```

### supports() 가 왜 필요한가

같은 컨트롤러에서 여러 커맨드 객체를 다룰 수 있다.
Validator 가 어떤 타입을 처리하는지 명시해야 스프링이 잘못된 객체에 적용하지 않는다.

`isAssignableFrom` 은 "이 클래스가 RegisterRequestForm 또는 그 하위 타입인가?" 를 검사한다.

## 6. ValidationUtils와 rejectValue

### ValidationUtils

비어 있는 필드 검사를 짧게 해주는 유틸리티.

```java
ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required.");
ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "confirmPassword.required");
```

| 메서드 | 설명 |
|---|---|
| `rejectIfEmpty(errors, field, code)` | 빈 문자열/null 이면 오류 등록 |
| `rejectIfEmptyOrWhitespace(errors, field, code)` | 빈 문자열/null/공백문자열 이면 오류 등록 |

### Errors / BindingResult 주요 메서드

| 메서드 | 설명 |
|---|---|
| `reject(errorCode)` | 객체 전체에 대한 오류 등록 (글로벌 에러) |
| `reject(errorCode, defaultMessage)` | 메시지 코드가 없을 때의 기본 메시지 |
| `rejectValue(field, errorCode)` | 특정 필드에 대한 오류 등록 |
| `rejectValue(field, errorCode, defaultMessage)` | 필드 + 기본 메시지 |
| `hasErrors()` | 오류가 하나라도 있는지 |
| `getFieldError(field)` | 특정 필드의 오류 객체 가져오기 |

## 7. 에러 코드 검색 규칙

`MessageSource` 는 `rejectValue("email", "email.invalid")` 처럼 등록된 에러 코드에 대해
다음 순서로 메시지 키를 검색한다.

1. `에러코드 + "." + 커맨드객체명 + "." + 필드명` → `email.invalid.registerRequest.email`
2. `에러코드 + "." + 필드명` → `email.invalid.email`
3. `에러코드` → `email.invalid`

즉 더 구체적인 키부터 찾아본다.
보통은 가장 일반적인 단계(3번)에 키를 두고 다국어 메시지를 작성한다.

`messages.properties`:

```properties
email.invalid=이메일이 입력되지 않았거나 @문자가 존재하지 않습니다.
name.empty=이름이 입력되지 않았습니다.
confirmPassword.required=비밀번호 확인이 필요합니다.
confirmPassword.nomatch=비밀번호와 확인이 일치하지 않습니다.
password.bad=패스워드는 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 가짐
```

## 8. <form:errors>로 오류 출력

`Errors` 에 등록된 오류를 JSP 에서 표시하는 태그.

```jsp
<form:errors path="email" cssClass="error" />
```

- `path` : 어떤 필드의 오류를 출력할지
- `cssClass` : 출력 시 적용할 CSS 클래스
- 글로벌 에러(필드 없는 `reject(...)`) 를 출력하려면 `path` 생략 또는 `path="*"`

`<form:errors>` 태그는 내부적으로 `MessageSource` 를 통해 등록된 에러 코드를 메시지로 변환한 뒤 출력한다.

## 9. JSR-380 어노테이션과 @Valid

### JSR-380 (Bean Validation 2.0)

자바 객체의 유효성 검사를 위한 표준 규격.
스프링은 이 표준을 지원하므로 어노테이션만 붙여도 검증이 자동으로 적용된다.

### 주요 어노테이션

| 어노테이션 | 의미 |
|---|---|
| `@NotNull` | null 이 아니어야 함 |
| `@NotEmpty` | 빈 문자열/컬렉션이 아니어야 함 |
| `@NotBlank` | null/빈/공백문자열 모두 아니어야 함 |
| `@Size(min=, max=)` | 문자열/컬렉션 크기 범위 |
| `@Min(value)` / `@Max(value)` | 숫자 최소/최대값 |
| `@Email` | 이메일 형식 |
| `@Pattern(regexp=)` | 정규표현식 매칭 |
| `@Future` / `@Past` | 미래/과거 날짜 |
| `@Positive` / `@Negative` | 양수/음수 |
| `@AssertTrue` / `@AssertFalse` | boolean 값 검사 |
| `@DecimalMin` / `@DecimalMax` | BigDecimal 등 소수 범위 |

### build.gradle

```gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

### 사용 예

```java
public class RegisterRequestForm {
    @Email(message = "이메일이 null이거나 양식이 일치하지 않습니다.")
    private String email;

    @Size(min = 2, max = 50, message = "이름은 반드시 2~50문자로 구성합니다.")
    private String name;

    @Size(min = 5, max = 20, message = "암호는 반드시 5~20문자로 구성됩니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{5,20}$",
            message = "암호는 반드시 특수문자 및 숫자를 포함합니다.")
    private String password;
}
```

### 컨트롤러에서 사용

```java
@PostMapping("/userSubmit")
public String userSubmit(
        @Valid @ModelAttribute("registerRequest") RegisterRequestForm registerRequest,
        BindingResult result) {
    if (result.hasErrors()) {
        return "week13/registerForm";
    }
    return "week13/welcome";
}
```

`@Valid` 가 붙으면 스프링이 `LocalValidatorFactoryBean` 을 통해 JSR-380 어노테이션을 자동 적용한다.

## 10. 글로벌 Validator와 @Validated

### 개념

같은 Validator 를 여러 컨트롤러에서 반복 호출하기 싫을 때, 전역에 한 번만 등록한다.

```java
@Configuration
public class MvcConfig2 implements WebMvcConfigurer {
    @Override
    public Validator getValidator() {
        return new RegisterRequestValidator();
    }
}
```

### @Validated 어노테이션

```java
@PostMapping("/globalValidatorSubmit")
public String globalValidatorSubmit(
        @Validated @ModelAttribute("registerRequest") RegisterRequestForm regReq,
        Errors errors) {
    if (errors.hasErrors()) {
        return "week13/errorMessage";
    }
    return "week13/welcome";
}
```

- `@Validated` 는 `@Valid` 와 유사하지만 검증 그룹(`groups = {...}`) 을 지원
- 글로벌 Validator 가 등록된 상태에서 `@Validated` 를 붙이면 그 Validator 가 자동 적용

### 주의: 기본 JSR-380 Validator 가 덮어쓰여짐

`getValidator()` 를 오버라이드하면 기본 `LocalValidatorFactoryBean` 이 교체된다.
즉 JSR-380 어노테이션 검증(`@Email`, `@Size` 등) 이 더 이상 동작하지 않는다.
글로벌 Validator 를 사용하는 학습 시에는 이 점을 인지해야 한다.

현재 프로젝트의 `MvcConfig2` 는 `getValidator()` 를 **주석 처리한 상태**로 둔다(원본 강의 자료와 동일).
학생이 Ex4 학습 시점에만 주석을 풀어 동작 차이를 확인하도록 의도했다.

## 11. @DateTimeFormat / @NumberFormat

### 왜 필요한가

요청 파라미터는 모두 String 으로 도착한다.
스프링은 `Integer`, `Long`, `Double` 같은 기본 타입은 자동 변환하지만, `LocalDateTime` 은 추가 설정 없이 변환되지 않는다.

`@DateTimeFormat`, `@NumberFormat` 어노테이션이 그 변환 규칙을 알려준다.

### @DateTimeFormat

```java
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime dateTime;
```

| 속성 | 설명 |
|---|---|
| `pattern` | 직접 패턴 지정 |
| `iso` | ISO 표준 형식 (`DATE`, `TIME`, `DATE_TIME`) |
| `style` | 짧은/긴 스타일 |

### @NumberFormat

```java
@NumberFormat(pattern = "#,###.##")
private Double number;

@NumberFormat(style = NumberFormat.Style.CURRENCY)
private Double price;

@NumberFormat(style = NumberFormat.Style.PERCENT)
private Double percentage;
```

| 스타일 | 설명 |
|---|---|
| `NUMBER` | 일반 숫자 |
| `CURRENCY` | 통화 (로케일에 따라 통화 기호) |
| `PERCENT` | 퍼센트 (0.5 → 50%) |

### 컨트롤러 메서드 파라미터에도 적용 가능

```java
@RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTime
```

### JSP 에서 출력 시 형식화

JSP 는 데이터 변환을 자동으로 하지 않는다.
출력 시 형식화는 `<fmt:formatNumber>`, `<fmt:formatDate>` 태그를 쓴다.

```jsp
<fmt:formatNumber value="${Price}" type="currency" currencyCode="KRW" maxFractionDigits="2" />
<fmt:formatDate value="${dateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
```

주의: `<fmt:formatDate>` 는 `java.util.Date` 타입을 요구하므로 `LocalDateTime` 을 변환해 모델에 담아야 한다.

```java
Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
model.addAttribute("dateTime", date);
```

## 12. 현재 프로젝트에서 구현한 실습 구조

원본 강의 코드는 한 번에 하나의 Ex 만 활성화하는 패턴이지만,
현재 학습 프로젝트는 학생이 모든 실습을 동시에 비교할 수 있도록 **각 Ex를 서로 다른 URL 로 분리**했다.

### 12-1. Ex1 - `<spring:message>` + `<form:form>` 데모

URL: `GET /week13/message` → 폼 화면, 폼 action 은 `/week13/membershipSubmit`
URL: `POST /week13/membershipSubmit` → 검증 없이 welcome 화면 출력

확인할 것:

- `<spring:message code="member.info" />` 가 messages.properties 의 값으로 치환
- `<form:input path="email">` 가 `${registerRequest.email}` 자동 출력

### 12-2. Ex2 - 수동 Validator

URL: `GET /week13/validation` → 폼 action 은 `/week13/membershipValidate`
URL: `POST /week13/membershipValidate` → `new RegisterRequestValidator().validate(...)` 호출

확인할 것:

- 컨트롤러가 직접 Validator 인스턴스를 생성해 호출
- `rejectValue("email", "email.invalid")` 호출 시 `email.invalid` 키가 messages.properties 에서 메시지로 변환
- 오류 시 폼 다시 표시, 각 필드 옆에 `<form:errors>` 메시지 출력

### 12-3. Ex3 - @Valid (JSR-380)

URL: `GET /week13/register` → 폼 action 은 `/week13/userSubmit`
URL: `POST /week13/userSubmit` → `@Valid` 자동 검증

확인할 것:

- `RegisterRequestForm` 의 `@Email`, `@Size`, `@Pattern` 어노테이션이 적용
- 어노테이션의 `message` 속성이 그대로 출력

### 12-4. Ex4 - 글로벌 Validator (@Validated)

URL: `GET /week13/globalValidator` → 폼 action 은 `/week13/globalValidatorSubmit`
URL: `POST /week13/globalValidatorSubmit` → `@Validated` 자동 검증

확인할 것:

- `MvcConfig2.getValidator()` 가 주석 상태이므로 기본은 JSR-380 동작 (Ex3 와 동일)
- 주석을 풀면 `RegisterRequestValidator` 가 글로벌로 적용되어 JSR-380 가 가려짐

### 12-5. Ex5 - 타입 변환

URL: `GET /week13/convert?number=1234.56&price=1234.56&percentage=0.156&dateTime=2026-05-26+12:34:56`

확인할 것:

- `@DateTimeFormat` 으로 문자열이 `LocalDateTime` 으로 변환
- `@NumberFormat` 으로 Double 이 통화/퍼센트 형식으로 표시
- JSTL `<fmt:formatNumber>` 가 출력 형식을 결정

## 13. 자주 헷갈리는 질문

### Q1. `@Valid` 와 `@Validated` 차이는?

- 둘 다 검증을 트리거
- `@Valid` 는 JSR 표준
- `@Validated` 는 스프링 확장, 검증 그룹(`groups`) 지원
- 글로벌 Validator 와 함께 쓸 때 보통 `@Validated` 를 쓴다.

### Q2. `Errors` 와 `BindingResult` 는?

- `BindingResult` 는 `Errors` 를 상속
- 컨트롤러에서는 보통 `BindingResult` 를 쓰면 추가 메서드(검증 대상 객체, 글로벌 에러 등)도 활용 가능

### Q3. `rejectValue("password", "password.bad")` 가 messages.properties 를 자동 검색하는가?

그렇다.
`<form:errors path="password">` 태그가 `password.bad` 를 키로 `MessageSource` 에서 메시지를 가져온다.

### Q4. `LocalValidatorFactoryBean` 이 자동 등록되는가?

스프링 부트가 `spring-boot-starter-validation` 의존성을 보면 자동으로 만든다.
이 빈이 `@Valid`/`@Validated` 처리를 담당한다.

### Q5. `getValidator()` 오버라이드는 항상 필요한가?

아니다.
글로벌 Validator 를 모든 컨트롤러에 일괄 적용하고 싶을 때만 사용한다.
오버라이드하면 기본 JSR-380 Validator 가 교체된다는 점을 반드시 인지해야 한다.

### Q6. `@NumberFormat(style = CURRENCY)` 가 ₩ 또는 $ 중 무엇을 출력하는가?

현재 `Locale` 에 따라 다르다.
JSP 에서 `<fmt:setLocale value="en_US" />` 또는 `ko_KR` 로 지정해 통화 기호를 제어할 수 있다.

### Q7. `LocalDateTime` 그대로 `<fmt:formatDate>` 에 넘기면?

오류가 발생한다.
`<fmt:formatDate>` 는 `java.util.Date` 만 받으므로 컨트롤러에서 `Date.from(...)` 으로 변환해 모델에 담아야 한다.

## 14. 시험 대비 핵심 정리

- `<spring:message>` 는 `MessageSource.getMessage(code, args, locale)` 을 호출하는 태그다.
- `<form:form modelAttribute="...">` 내부에서 `<form:input path="...">` 가 커맨드 객체 필드와 매핑된다.
- 검증 인터페이스 3총사: `Validator`, `Errors`, `BindingResult`.
- `Validator.supports(clazz)` 는 검증 대상 타입을 가린다.
- `Validator.validate(target, errors)` 는 오류를 `errors.rejectValue(field, code)` 로 등록한다.
- `ValidationUtils.rejectIfEmpty/rejectIfEmptyOrWhitespace` 가 빈 필드 검사용 유틸이다.
- 에러 코드는 `에러코드.객체명.필드명` → `에러코드.필드명` → `에러코드` 순서로 메시지 키가 검색된다.
- JSR-380 어노테이션은 커맨드 객체 필드에 직접 붙이고 `@Valid` 로 트리거한다.
- `@Validated` 는 검증 그룹 지원이 추가된 스프링 확장이다.
- `WebMvcConfigurer.getValidator()` 는 글로벌 Validator 를 설정하지만 기본 JSR-380 Validator 를 교체한다는 점을 유의한다.
- `@DateTimeFormat` / `@NumberFormat` 은 요청 파라미터 String 을 다른 타입으로 변환한다.
- JSP 출력 형식화는 별도로 `<fmt:formatNumber>`, `<fmt:formatDate>` 태그가 필요하다.
- `<fmt:formatDate>` 는 `java.util.Date` 타입을 요구한다.
