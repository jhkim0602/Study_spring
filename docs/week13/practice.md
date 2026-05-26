# Week 13 Practice

## 주제

13주차 실습은 제공된 `BasicProcessController2`, `RegisterRequestForm`, `RegisterRequestValidator`, `FormatCommand`, `MvcConfig2` 흐름을 현재 프로젝트 구조에 맞게 재작성한 것이다.

원본 실습은 강의자료의 Ex1~Ex5 흐름을 따라가며, 각 Ex 가 동일 URL `/membershipSubmit` 을 공유하면서 주석으로 한 가지씩만 활성화하는 패턴이다.

현재 학습 프로젝트는 학생이 모든 실습을 동시에 비교할 수 있도록 **각 Ex 를 서로 다른 URL 로 분리**했다.

- Ex1: `<spring:message>` + `<form:form>` 데모 (검증 없음)
- Ex2: 수동 Validator 사용
- Ex3: `@Valid` + JSR-380 어노테이션
- Ex4: 글로벌 Validator (`@Validated`)
- Ex5: `@DateTimeFormat` / `@NumberFormat` 타입 변환

## 이 실습의 목적

13주차 실습의 목표는 "같은 폼 데이터에 대해 4가지 검증 방식을 직접 비교"하는 것이다.

- 메시지/폼 태그가 하드코딩 대비 무엇을 줄이는지
- 수동 Validator 의 `supports()`/`validate()` 가 어떻게 동작하는지
- JSR-380 어노테이션이 어떻게 검증을 자동화하는지
- 글로벌 Validator 의 효과와 함정(JSR-380 가려짐)
- `@DateTimeFormat`/`@NumberFormat` 의 변환 동작과 JSP 출력 형식화 차이

## 관련 파일

| 경로 | 역할 |
|---|---|
| [`src/main/java/Lect_B/week13/RegisterRequestForm.java`](../../src/main/java/Lect_B/week13/RegisterRequestForm.java) | 검증 대상 커맨드 객체 (JSR-380 어노테이션) |
| [`src/main/java/Lect_B/week13/RegisterRequestValidator.java`](../../src/main/java/Lect_B/week13/RegisterRequestValidator.java) | 수동 Validator 구현 |
| [`src/main/java/Lect_B/week13/FormatCommand.java`](../../src/main/java/Lect_B/week13/FormatCommand.java) | 타입 변환 어노테이션 커맨드 객체 |
| [`src/main/java/Lect_B/week13/MvcConfig2.java`](../../src/main/java/Lect_B/week13/MvcConfig2.java) | 글로벌 Validator 설정 (주석 상태) |
| [`src/main/java/Lect_B/week13/BasicProcessController2.java`](../../src/main/java/Lect_B/week13/BasicProcessController2.java) | Ex1~Ex5 라우트 컨트롤러 |
| [`src/main/java/Lect_B/week13/Week13IndexController.java`](../../src/main/java/Lect_B/week13/Week13IndexController.java) | `/week13` 진입 컨트롤러 |
| [`src/main/resources/messages.properties`](../../src/main/resources/messages.properties) | 에러 메시지 / 라벨 키 |
| [`src/main/webapp/views/week13/*.jsp`](../../src/main/webapp/views/week13) | 5개 JSP (index, registerForm, welcome, errorMessage, formatResult) |
| [`src/test/java/Lect_B/week13/Week13ContextTests.java`](../../src/test/java/Lect_B/week13/Week13ContextTests.java) | 빈 등록, Validator 동작 테스트 |

원본 13주차 실습 파일과 현재 프로젝트의 대응 관계는 다음과 같다.

| 원본 실습 파일 | 원본 의도 | 현재 프로젝트 반영 |
|---|---|---|
| `BasicProcessController2.java` (`Lect.week13`) | Ex1~Ex5 통합 컨트롤러 | `Lect_B.week13.BasicProcessController2`, 각 Ex 를 서로 다른 URL 로 분리 |
| `MvcConfig2.java` | `getValidator()` 주석 처리 | 동일 (Ex4 학습 시점에만 주석 풀기) |
| `RegisterRequestForm.java` | JSR-380 어노테이션 적용 | 동일 |
| `RegisterRequestValidator.java` | 수동 Validator 구현 | 동일, password null 안전성 보강 |
| `FormatCommand.java` | `@DateTimeFormat`/`@NumberFormat` | 동일 |
| `view/registerForm.jsp` | 폼 화면 | `views/week13/registerForm.jsp` 로 이동, `formAction` 모델로 폼 action 동적 처리 |
| `view/welcome.jsp` | 가입 완료 화면 | `views/week13/welcome.jsp` |
| `view/formatResult.jsp` | 변환 결과 표시 | `views/week13/formatResult.jsp`, JSTL `jakarta.tags.fmt` URI 로 갱신 |
| `resources/messages.properties` | 메시지 키 | 11주차 작업으로 이미 존재, 재사용 |

## 1. 왜 `week13` 전용 패키지를 따로 만들었는가

13주차는 다음과 같은 새로운 기능을 한꺼번에 도입한다.

- `Validator` 인터페이스 및 `Errors`/`BindingResult`
- JSR-380 어노테이션 검증 (`spring-boot-starter-validation`)
- 글로벌 Validator 설정
- `@DateTimeFormat` / `@NumberFormat`

이 빈들이 11주차 폼 처리와 분리되도록 새 패키지를 만들었다.

```text
src/main/java/Lect_B/week13/
src/main/webapp/views/week13/
src/test/java/Lect_B/week13/
docs/week13/
```

## 2. `BasicProcessController2`의 라우트 한 줄씩 읽기

### 2-1. Ex1 - 폼 화면 4종 + 단순 제출

```java
@GetMapping({ "/message", "/validation", "/register", "/globalValidator" })
public String membershipForm(jakarta.servlet.http.HttpServletRequest request, Model model) {
    String uri = request.getRequestURI();
    String formAction;
    if (uri.endsWith("/validation")) {
        formAction = "/week13/membershipValidate";   // Ex2
    } else if (uri.endsWith("/register")) {
        formAction = "/week13/userSubmit";           // Ex3
    } else if (uri.endsWith("/globalValidator")) {
        formAction = "/week13/globalValidatorSubmit"; // Ex4
    } else {
        formAction = "/week13/membershipSubmit";     // Ex1
    }
    model.addAttribute("registerRequest", new RegisterRequestForm());
    model.addAttribute("formAction", formAction);
    return "week13/registerForm";
}
```

- 4가지 GET URL 이 동일한 `registerForm.jsp` 를 사용
- 진입한 URL 에 따라 폼 action 만 다르게 모델에 담음
- 비어 있는 `RegisterRequestForm` 을 같이 담아 폼 필드 초기값 처리

### 2-2. Ex1 - 단순 제출

```java
@PostMapping("/membershipSubmit")
public String membershipSubmit(RegisterRequestForm regReq, Model model) {
    model.addAttribute("registerRequest", regReq);
    return "week13/welcome";
}
```

- 검증 없이 입력값 그대로 welcome 화면에 출력
- `<spring:message code="register.done">` + `<spring:argument>` 매개변수 인덱스 매핑 학습용

### 2-3. Ex2 - 수동 Validator

```java
@PostMapping("/membershipValidate")
public String membershipValidate(
        @ModelAttribute("registerRequest") RegisterRequestForm regReq,
        BindingResult result,
        Model model) {
    model.addAttribute("registerRequest", regReq);
    model.addAttribute("formAction", "/week13/membershipValidate");
    new RegisterRequestValidator().validate(regReq, result);
    if (result.hasErrors()) {
        return "week13/registerForm";
    }
    return "week13/welcome";
}
```

- 컨트롤러가 직접 Validator 인스턴스를 만들어 호출
- 오류 발생 시 폼 다시 표시, JSP 의 `<form:errors>` 가 메시지 키를 자동 변환

### 2-4. Ex3 - @Valid

```java
@PostMapping("/userSubmit")
public String userSubmit(
        @Valid @ModelAttribute("registerRequest") RegisterRequestForm registerRequest,
        BindingResult result,
        Model model) {
    System.out.println("@Valid 오류 수 : " + result.getErrorCount());
    model.addAttribute("formAction", "/week13/userSubmit");
    if (result.hasErrors()) {
        return "week13/registerForm";
    }
    return "week13/welcome";
}
```

- `@Valid` 가 JSR-380 어노테이션을 자동 적용
- `RegisterRequestForm` 의 `@Email`, `@Size`, `@Pattern` 메시지가 출력

### 2-5. Ex4 - 글로벌 Validator (@Validated)

```java
@PostMapping("/globalValidatorSubmit")
public String globalValidatorSubmit(
        @Validated @ModelAttribute("registerRequest") RegisterRequestForm regReq,
        Errors errors,
        Model model) {
    model.addAttribute("registerRequest", regReq);
    if (errors.hasErrors()) {
        return "week13/errorMessage";
    }
    return "week13/welcome";
}
```

- `MvcConfig2.getValidator()` 가 주석 상태이므로 기본은 JSR-380 (Ex3 와 동일 동작)
- 학생이 Ex4 학습 시점에 `MvcConfig2` 의 주석을 풀면 `RegisterRequestValidator` 가 글로벌로 적용
- 오류 시 `errorMessage.jsp` 로 이동

### 2-6. Ex5 - 타입 변환

```java
@GetMapping("/convert")
public String sampleFormat(
        @RequestParam("number") @NumberFormat(style = NumberFormat.Style.NUMBER) Double number,
        @RequestParam("price") @NumberFormat(style = NumberFormat.Style.CURRENCY) Double price,
        @RequestParam("percentage") @NumberFormat(style = NumberFormat.Style.PERCENT) Double percentage,
        @RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTime,
        @ModelAttribute FormatCommand format,
        Model model) {
    // ...
    Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    model.addAttribute("dateTime", date);
    format.setDate(date);
    model.addAttribute("obj", format);
    return "week13/formatResult";
}
```

- `@RequestParam` 위의 `@NumberFormat`, `@DateTimeFormat` 으로 String → 다른 타입 변환
- `@ModelAttribute FormatCommand` 는 같은 쿼리 파라미터를 객체로도 자동 바인딩 (필드의 어노테이션이 동작)
- `LocalDateTime` 을 JSTL `<fmt:formatDate>` 가 받을 수 있는 `Date` 로 변환

## 3. `MvcConfig2` 한 줄씩 읽기

```java
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
```

- `@Configuration` 으로 스프링이 빈으로 등록
- `getValidator()` 가 주석 상태이므로 기본 JSR-380 Validator(`LocalValidatorFactoryBean`) 가 그대로 사용
- 학생이 Ex4 학습 시점에 주석을 풀어 동작 차이를 직접 확인

## 4. JSP 화면이 무엇을 보여 주는가

| JSP | 역할 |
|---|---|
| `views/week13/index.jsp` | 13주차 메뉴 + 타입 변환 링크 자동 생성 |
| `views/week13/registerForm.jsp` | `<form:form>` / `<form:errors>` 폼 화면, `${formAction}` 으로 폼 action 동적 표시 |
| `views/week13/welcome.jsp` | `<spring:message>` 매개변수 인자 처리 데모, 가입 완료 메시지 |
| `views/week13/errorMessage.jsp` | Ex4 글로벌 검증 실패 시 메시지 출력 |
| `views/week13/formatResult.jsp` | JSTL `<fmt:formatNumber>`, `<fmt:formatDate>` 로 형식 출력 |

## 5. 실행 결과 보는 법

### 5-1. Ex1 흐름

1. `/week13` → 13주차 메뉴
2. `/week13/message` → registerForm 진입 (action: `/week13/membershipSubmit`)
3. 폼 작성 후 제출 → welcome 화면, `<spring:message>` 인자 처리 결과 표시

### 5-2. Ex2 흐름 (정상)

1. `/week13/validation` → 폼 action `/week13/membershipValidate`
2. 정상 입력 (예: `Abcd1234!` 비밀번호 + 일치 확인) → welcome 화면

### 5-3. Ex2 흐름 (오류)

1. `/week13/validation` → 폼
2. 이메일에 `@` 없이 입력, 이름 비움, 짧은 비밀번호로 제출
3. 폼이 다시 표시되며 각 필드 옆에 `<form:errors>` 메시지 출력:
   - 이메일: `이메일이 입력되지 않았거나 @문자가 존재하지 않습니다.`
   - 이름: `이름이 입력되지 않았습니다.`
   - 비밀번호: `패스워드는 최소 8자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 가짐`

### 5-4. Ex3 흐름 (오류)

1. `/week13/register` → 폼 action `/week13/userSubmit`
2. 잘못된 값 입력 후 제출
3. JSR-380 어노테이션의 `message` 속성이 출력:
   - 이메일: `이메일이 null이거나 양식이 일치하지 않습니다.`
   - 이름: `이름은 반드시 2~50문자로 구성합니다.`
   - 비밀번호: `암호는 반드시 5~20문자로 구성됩니다.` + `암호는 반드시 특수문자 및 숫자를 포함합니다.`

### 5-5. Ex4 흐름

1. `/week13/globalValidator` → 폼 action `/week13/globalValidatorSubmit`
2. **현재 상태**: `MvcConfig2.getValidator()` 주석 → JSR-380 어노테이션 동작 (Ex3 와 동일 메시지)
3. **글로벌 Validator 학습 시**: `MvcConfig2` 의 주석을 풀고 재기동 → 오류 시 `errorMessage.jsp` 로 이동, `RegisterRequestValidator` 메시지 출력

### 5-6. Ex5 흐름

1. `/week13` 메뉴에서 "타입 변환 예제" 링크 클릭
2. 또는 URL 직접 입력: `/week13/convert?number=1234.56&price=99999.99&percentage=0.156&dateTime=2026-05-26+12:34:56`
3. 결과 화면에서 다음을 확인:
   - Number: `1,234.56` (천 단위 구분)
   - Price (KRW): `₩99,999.99` (통화)
   - Percent: `15.6%` (퍼센트 변환)
   - dateTime: `2026-05-26 12:34:56` (형식화된 날짜)

## 6. 강의자료와의 차이점 정리

- 원본 패키지 `Lect.week13` → 본 프로젝트 `Lect_B.week13` (프로젝트 컨벤션)
- 모든 라우트에 `/week13/` 접두어 추가 (다른 주차와의 일관성)
- 원본 한 URL 공유 + 주석 토글 → 각 Ex 별 서로 다른 URL 로 분리하여 동시 비교 가능
- 폼 action 을 모델 변수 `${formAction}` 으로 처리 → 동일 JSP 에서 4개 Ex 모두 처리
- `RegisterRequestValidator` 에 `password == null` 안전성 보강 (NPE 방지)
- 원본 `BasicProcessController2.java` 의 잘못된 import (`com.week11.RegisterRequest`) 제거
- 원본 `registerForm.jsp` 의 `massage` 오타 (`message` 의도) 를 URL `/week13/message` 로 정정
- 원본의 `<fmt>` taglib URI 가 `http://java.sun.com/jsp/jstl/fmt` → 현재 Spring Boot 4 환경에 맞춰 `jakarta.tags.fmt` 로 변경

## 7. 테스트로 확인하기

`Week13ContextTests` 는 다음을 자동으로 검증한다.

| 테스트 | 확인 내용 |
|---|---|
| `week13BeansAreLoaded` | `BasicProcessController2`, `MvcConfig2` 빈 등록 |
| `registerRequestFormPasswordMatch` | `isPasswordEqualToConfirmPassword()` 동작 |
| `registerRequestValidatorSupportsTargetType` | `supports()` 가 `RegisterRequestForm` 만 true |
| `registerRequestValidatorRejectsInvalidEmail` | `@` 없는 이메일에 `email.invalid` 에러 등록 |
| `registerRequestValidatorRejectsWeakPassword` | 정규표현식 미일치 시 `password.bad` 등록 |
| `registerRequestValidatorRejectsConfirmPasswordMismatch` | 비밀번호 불일치 시 `confirmPassword.nomatch` 등록 |
| `registerRequestValidatorAcceptsValidForm` | 정상 입력 시 오류 없음 |
| `formatCommandSettersAndGettersWork` | Lombok `@Getter`/`@Setter` 동작 |

## 8. 학습 흐름 보존 장치

- `MvcConfig2.getValidator()` 주석 처리 → 원본 강의자료와 동일, Ex4 학습 시점에만 주석 풀기
- `BasicProcessController2` 클래스 상단 주석에 Ex1~Ex4 가 어떤 URL 로 분리됐는지 명시
- `messages.properties` 의 키는 11주차에서 이미 추가됨 → 재사용
