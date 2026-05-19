# Week 11 Theory

## 주제

11주차는 **요청 매핑, 커맨드 객체, 메시지 다국화**를 다룬다.

지금까지 컨트롤러는 단순 GET 요청 한두 개를 처리했다.
11주차는 실제 회원가입 흐름처럼 여러 단계의 폼과 다양한 파라미터, 여러 언어를 다루는 패턴을 한 번에 정리한다.

## 이 문서를 읽기 전에

11주차를 이해하려면 다음 흐름을 기억해야 한다.

- 2주차: 컨트롤러와 JSP는 어떻게 연결되는가
- 4주차: 컨트롤러는 빈을 DI로 주입받는다
- 10주차: 같은 컨트롤러가 데이터베이스와도 연결될 수 있다

11주차는 이 흐름 위에서 다음 질문으로 넘어간다.

> "한 컨트롤러가 GET/POST, 단순 값/객체/리스트/중첩 객체, 다국어 메시지를 모두 처리하려면?"

## 현재 프로젝트에서 먼저 볼 코드

- [`BasicProcessController1.java`](../../src/main/java/Lect_B/week11/BasicProcessController1.java)
- [`MvcConfig.java`](../../src/main/java/Lect_B/week11/MvcConfig.java)
- [`RegisterRequest.java`](../../src/main/java/Lect_B/week11/RegisterRequest.java)
- [`Question.java`](../../src/main/java/Lect_B/week11/Question.java)
- [`AnsweredData.java`](../../src/main/java/Lect_B/week11/AnsweredData.java)
- [`Respondent.java`](../../src/main/java/Lect_B/week11/Respondent.java)
- [`Week11IndexController.java`](../../src/main/java/Lect_B/week11/Week11IndexController.java)
- [`messages.properties`](../../src/main/resources/messages.properties), [`messages_ko.properties`](../../src/main/resources/messages_ko.properties), [`messages_en.properties`](../../src/main/resources/messages_en.properties)
- [`src/main/webapp/views/week11`](../../src/main/webapp/views/week11)

## 목차

- [1. 11주차의 핵심 질문](#1-11주차의-핵심-질문)
- [2. 요청 매핑 어노테이션](#2-요청-매핑-어노테이션)
- [3. 컨트롤러 메서드가 받을 수 있는 파라미터](#3-컨트롤러-메서드가-받을-수-있는-파라미터)
- [4. @RequestParam과 @PathVariable](#4-requestparam과-pathvariable)
- [5. 리다이렉트와 RedirectAttributes](#5-리다이렉트와-redirectattributes)
- [6. 커맨드 객체와 @ModelAttribute](#6-커맨드-객체와-modelattribute)
- [7. 리스트와 중첩 객체를 가진 커맨드 객체](#7-리스트와-중첩-객체를-가진-커맨드-객체)
- [8. 컨트롤러 없이 경로 매핑하기](#8-컨트롤러-없이-경로-매핑하기)
- [9. 메시지 다국화](#9-메시지-다국화)
- [10. 현재 프로젝트에서 구현한 실습 구조](#10-현재-프로젝트에서-구현한-실습-구조)
- [11. 자주 헷갈리는 질문](#11-자주-헷갈리는-질문)
- [12. 시험 대비 핵심 정리](#12-시험-대비-핵심-정리)

## 1. 11주차의 핵심 질문

11주차를 한 문장으로 요약하면:

> "컨트롤러는 GET/POST, 단순 값, 객체, 리스트, 중첩 객체, 다국어 메시지를 모두 받을 수 있다"

이다.

학생은 이 주차에서 강의자료의 Ex1~Ex6를 따라가며 각 패턴을 한 번씩 직접 호출해 본다.

## 2. 요청 매핑 어노테이션

`@Controller`가 붙은 클래스의 메서드에 요청 매핑 어노테이션을 붙여 경로를 지정한다.

| 어노테이션 | 의미 |
|---|---|
| `@RequestMapping("/path")` | GET/POST 등 모든 메서드 허용 (또는 `method=...`로 제한) |
| `@GetMapping("/path")` | GET 전용 |
| `@PostMapping("/path")` | POST 전용 |
| `@PutMapping`, `@DeleteMapping` | PUT, DELETE 전용 |

`@RequestMapping`은 클래스에 붙여 공통 경로를 지정할 수 있다.

```java
@Controller
@RequestMapping("regist")
public class BasicProcessController1 {

    @GetMapping("/step/{id}")
    public String detail(@PathVariable("id") Long stepId) { ... }

    @PostMapping("/step2")
    public String handleStep2(...) { ... }
}
```

읽는 법:

- 클래스 레벨 `@RequestMapping("regist")` → 공통 prefix
- 메서드 레벨 `/step/{id}` → 최종 경로는 `/regist/step/{id}`

강의자료의 핵심 슬라이드는 다음을 비교한다.

```java
// 같은 컨트롤러 안에서 메서드별로 경로를 모두 적는 방식
@RequestMapping("/register/step1")
@PostMapping("/register/step2")
@GetMapping("/register/step2")

// 클래스 레벨에 공통 경로를 두는 방식 (현재 프로젝트 사용)
@RequestMapping("/register")
public class RegisterController {
    @RequestMapping("/step1")
    @GetMapping("/step2")
}
```

## 3. 컨트롤러 메서드가 받을 수 있는 파라미터

요청 매핑 어노테이션이 붙은 메서드는 매우 다양한 타입의 파라미터를 받을 수 있다.

강의자료에서 다루는 주요 타입을 표로 정리한다.

| 파라미터 종류 | 어노테이션 | 용도 |
|---|---|---|
| 단순 요청 파라미터 | `@RequestParam` | 쿼리스트링/폼 단일 값 |
| 경로 변수 | `@PathVariable` | URL 일부를 변수로 |
| 커맨드 객체 | `@ModelAttribute` 또는 생략 | 자바 빈으로 자동 바인딩 |
| 모델 객체 | `Model`, `ModelAndView` | 뷰에 데이터 전달 |
| 리다이렉트 데이터 | `RedirectAttributes` | 리다이렉트 직후 1회용 데이터 |
| 로케일 | `Locale` | 현재 로케일 |
| 세션/요청 정보 | `HttpServletRequest`, `HttpSession` | 저수준 접근 필요 시 |

특히 단순 값과 커맨드 객체를 구분하는 것이 중요하다.

## 4. @RequestParam과 @PathVariable

### @RequestParam

```java
@GetMapping("/search")
public String search(@RequestParam("query") String query,
                     @RequestParam("pageNo") int pageNo) { ... }
```

- 쿼리스트링 또는 폼 데이터의 단일 값을 받는다
- 타입이 자동 변환된다 (String → int 등)
- 변환 실패 시 400 응답
- 기본은 **필수**다. 없으면 400. `required=false`로 옵션 처리 가능
- `defaultValue="..."`로 기본값 지정 가능 (이때 자동으로 optional 처리됨)

현재 프로젝트의 예:

```java
@PostMapping("/step2")
public String handleStep2(
        @RequestParam("view") String view,
        @RequestParam(value = "agree", defaultValue = "false") Boolean agree,
        RedirectAttributes redirectAttributes) { ... }
```

### @PathVariable

```java
@GetMapping("/step/{id}")
public String detail(@PathVariable("id") Long stepId) { ... }
```

- URL의 `{id}` 자리에 들어온 값을 받는다
- 매개변수 이름이 같으면 `("id")` 생략 가능
- 타입 변환은 `@RequestParam`과 동일

### 둘의 차이

- `@RequestParam`은 쿼리스트링/폼 데이터를 받음 (`?id=3`, `<input name="id">`)
- `@PathVariable`은 URL 경로 자체에서 값을 받음 (`/step/3`)

RESTful URL에서는 `@PathVariable`을 자주 쓴다.

## 5. 리다이렉트와 RedirectAttributes

POST 후 다른 URL로 이동하려면 `redirect:` 접두어를 쓴다.

```java
return "redirect:/regist/step/1";
```

이때 한 가지 문제가 있다.

- 리다이렉트는 새로운 HTTP 요청이다
- 그래서 이전 `model.addAttribute(...)`로 넘긴 데이터는 사라진다
- 쿼리스트링으로 붙이면 URL이 지저분해진다

해결책: `RedirectAttributes.addFlashAttribute(...)`

```java
if (!agree) {
    redirectAttributes.addFlashAttribute("message", "약관 동의를 해주세요.");
    return "redirect:/regist/step/1";
}
```

특징:

- 리다이렉트된 페이지에서 한 번만 살아남는다
- 세션을 통해 잠깐 보관됐다가 다음 요청에서 사용 후 사라진다
- 폼 검증 실패 메시지, "저장되었습니다" 같은 알림에 적합

현재 프로젝트에서 약관에 동의하지 않으면 `registerStep1.jsp`로 돌아가면서 "약관 동의를 해주세요." 메시지가 한 번 표시되고 사라지는 흐름이다.

## 6. 커맨드 객체와 @ModelAttribute

### 커맨드 객체란

HTML 폼의 `<input name="...">` 이름이 자바 빈 클래스의 프로퍼티(`setter`)와 같다면,
스프링 MVC는 자동으로 폼 데이터를 자바 빈 객체로 만들어 전달한다.
이 객체를 **커맨드 객체**라 부른다.

```html
<input type="text" name="email" value="...">
<input type="text" name="name" value="...">
<input type="password" name="password" value="...">
<input type="password" name="confirmPassword" value="...">
```

```java
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
    // getter, setter
}
```

```java
@PostMapping("/step3")
public String handleStep3(RegisterRequest registerRequest, Model model) { ... }
```

스프링은:

1. `RegisterRequest` 인스턴스를 만든다
2. 폼 데이터의 `email` 값을 `setEmail`로 넣는다
3. 모든 필드를 채운 객체를 메서드의 파라미터로 전달한다

이게 커맨드 객체 자동 바인딩이다.

### 뷰에서 커맨드 객체 사용

JSP에서는 클래스 이름을 카멜케이스로 바꾼 키로 접근한다.

```html
<input type="text" name="email" value="${registerRequest.email}">
```

기본 키 이름은 `registerRequest` (타입 이름의 첫 글자 소문자).
`@ModelAttribute("anotherName")`으로 변경 가능.

### @ModelAttribute를 메서드에 붙이면?

```java
@ModelAttribute("registerRequest")
public RegisterRequest initCommand(HttpServletRequest request) {
    RegisterRequest cm = new RegisterRequest();
    cm.setName("한글 이름을 입력해 주세요");
    cm.setEmail("규격에 맞춰서 이메일을 입력해 주세요");
    cm.setPassword("영문자 및 특수 문자 포함 최소 4문자 입력");
    cm.setConfirmPassword("암호를 다시 한번 입력해 주세요");
    return cm;
}
```

이 메서드는 같은 컨트롤러 안의 모든 요청 처리 메서드 실행 **전**에 호출된다.
반환값은 `Model`에 자동으로 추가된다.

용도:

- 폼 화면에 진입했을 때 초기값을 미리 채워 놓는다
- 모든 요청에서 공통으로 필요한 데이터를 준비한다

현재 프로젝트에서는 `/regist/initCommand` 진입 시 비어 있는 폼 대신 placeholder 텍스트가 미리 채워진 폼을 보여 준다.

### `@ModelAttribute`를 매개 변수에 붙이면?

```java
@PostMapping("/survey")
public String submit(@ModelAttribute("ansData") AnsweredData data) { ... }
```

- 폼 데이터를 자동 바인딩하면서
- 모델 키를 `"ansData"`로 명시한다
- JSP에서 `${ansData.responses}`로 접근

## 7. 리스트와 중첩 객체를 가진 커맨드 객체

폼이 더 복잡해지면 단일 필드뿐 아니라 리스트, 중첩 객체도 필요하다.

### 리스트 바인딩

```html
<input type="radio" name="responses[0]" value="서버">
<input type="radio" name="responses[1]" value="이클립스">
<input type="text"  name="responses[2]">
```

```java
public class AnsweredData {
    private List<String> responses;
}
```

스프링은 `responses[0]`, `responses[1]`을 보면 `List<String>`을 만들어 인덱스 위치에 값을 채운다.

### 중첩 객체 바인딩

```html
<input type="text" name="res.location">
<input type="text" name="res.age">
```

```java
public class AnsweredData {
    private List<String> responses;
    private Respondent res;
}

public class Respondent {
    private int age;
    private String location;
}
```

스프링은 `res.location`이라는 이름을 보고 `setRes(new Respondent())`를 호출하고 그 안의 `setLocation(...)`을 호출한다.

현재 프로젝트의 `/regist/survey` 흐름이 이 두 가지를 한 번에 보여 준다.

```java
@GetMapping("/survey")
public String form(Model model) {
    Question q1 = new Question("당신의 역할은 무엇입니까?", List.of("서버", "프론트", "풀스택"));
    Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?", List.of("이클립스", "인텔리J", "서브라임"));
    Question q3 = new Question("하고 싶은 말을 적어주세요.");
    model.addAttribute("questions", List.of(q1, q2, q3));
    return "week11/surveyForm";
}

@PostMapping("/survey")
public String submit(@ModelAttribute("ansData") AnsweredData data) {
    return "week11/surveySubmitted";
}
```

`Question` 클래스는 `@RequiredArgsConstructor`(Lombok)를 이용해 `final` 필드만 받는 생성자를 자동으로 만든다.

```java
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {
    private final String title;   // RequiredArgsConstructor 대상
    private List<String> options; // AllArgsConstructor에서만 채워짐

    public boolean isChoice() {
        return options != null && !options.isEmpty();
    }
}
```

JSP의 `${q.choice}`는 `isChoice()` 호출 결과다 (JSP EL의 boolean getter 규칙).

## 8. 컨트롤러 없이 경로 매핑하기

`/main` 같은 단순 진입 경로는 컨트롤러 메서드를 따로 만들지 않고 바로 뷰와 연결할 수 있다.

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main").setViewName("week11/welcome");
    }
}
```

장점:

- 메서드 안에 비즈니스 로직이 없을 때 컨트롤러 클래스 자체를 만들 필요가 없다
- 환영 화면, 약관 화면, 정적 안내 화면에 자주 사용

현재 프로젝트도 `/main` → `week11/welcome.jsp`, `/week11/localeResolver` → `week11/localeResolver.jsp`를 이 방식으로 연결한다.

## 9. 메시지 다국화

### 구성 요소

- `messages.properties`: 기본 메시지
- `messages_ko.properties`: 한국어 (`ko`, `ko_KR`)
- `messages_en.properties`: 영어 (`en`, `en_US`)

```properties
greeting=안녕하세요.hello
farewell=수고하셨습니다. 안녕히 가세요.
```

```properties
greeting=Hello
farewell=Goodbye
```

### MessageSource 빈

```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
    ms.setBasename("messages");
    ms.setDefaultEncoding("UTF-8");
    return ms;
}
```

- `basename`: 파일 이름 prefix (`messages` → `messages.properties`, `messages_ko.properties` 등)
- `defaultEncoding`: 파일 인코딩 (UTF-8 권장)

### LocaleResolver 빈

```java
@Bean
public SessionLocaleResolver localeResolver() {
    SessionLocaleResolver resolver = new SessionLocaleResolver();
    resolver.setDefaultLocale(Locale.KOREAN);
    return resolver;
}
```

세션 단위로 현재 사용자의 로케일을 기억한다.

### LocaleChangeInterceptor 빈

```java
@Bean
public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
}

@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
}
```

요청 쿼리스트링에 `?lang=en`이 있으면 자동으로 로케일을 변경한다.

### 메시지 조회

```java
@GetMapping("/message")
public String message(Locale locale, Model model) {
    String greeting = messageSource.getMessage("greeting", null, locale);
    String farewell = messageSource.getMessage("farewell", null, locale);
    model.addAttribute("greeting", greeting);
    model.addAttribute("farewell", farewell);
    return "week11/greeting";
}
```

`Locale`은 컨트롤러 메서드 파라미터로 자동 주입된다.
`messageSource.getMessage(key, args, locale)`로 해당 언어 메시지를 조회한다.

### 흐름 정리

1. 사용자가 `/regist/message?lang=ko` 호출
2. `LocaleChangeInterceptor`가 `lang=ko`를 보고 `LocaleResolver`에 한국어 로케일 저장
3. 컨트롤러 메서드의 `Locale locale` 파라미터에 한국어 로케일 주입
4. `messageSource.getMessage(..., locale)`이 `messages_ko.properties`를 조회
5. 결과를 JSP로 전달

## 10. 현재 프로젝트에서 구현한 실습 구조

11주차 실습은 원본 코드의 흐름을 유지하되, 현재 프로젝트 구조에 맞게 재구성했다.

### 10-1. Ex1 - 경로 매핑과 RedirectAttributes

URL: `/regist/step/1` → 폼 제출 → `/regist/step2` → 폼 제출 → `/regist/step3`

확인할 것:

- `@PathVariable`로 step 번호 수신
- 약관 미동의 시 `redirectAttributes.addFlashAttribute`로 flash 메시지 전달
- step2의 폼에서 `${registerRequest.email}` 등으로 placeholder 표시
- step3에서 `RegisterRequest`로 자동 바인딩

### 10-2. Ex2 - 컨트롤러 없이 ViewController로 뷰 매핑

URL: `/main`

확인할 것:

- `MvcConfig.addViewControllers`에 `/main` → `week11/welcome` 매핑
- 컨트롤러 클래스가 따로 없어도 동작

### 10-3. Ex3 - 커맨드 객체 자동 바인딩 (강의자료 참고)

현재 프로젝트는 Ex3의 일반 자동 바인딩 형태를 주석으로 보존하고,
명시적인 `@RequestParam` 폼 처리 메서드를 활성화해 두었다.
주석을 풀면 강의자료의 Ex3, Ex4 흐름을 직접 비교할 수 있다.

### 10-4. Ex4 - @ModelAttribute로 초기화

URL: `/regist/initCommand`

확인할 것:

- `@ModelAttribute("registerRequest") public RegisterRequest initCommand(...)`가 자동 실행됨
- step2 폼에 placeholder 텍스트가 미리 채워짐

### 10-5. Ex5 - 리스트/중첩 객체 커맨드

URL: `/regist/survey`

확인할 것:

- `responses[0]`, `responses[1]`, `responses[2]`가 `List<String> responses`에 인덱스 위치로 채워짐
- `res.location`, `res.age`가 `Respondent res` 객체로 바인딩
- 결과 화면(`/regist/survey` POST → `surveySubmitted.jsp`)에서 응답 내용 표시

### 10-6. Ex6 - 메시지 다국화

URL: `/regist/message?lang=ko`, `/regist/message?lang=en`

확인할 것:

- `messages_ko.properties`, `messages_en.properties` 파일을 각각 조회
- 쿼리스트링의 `lang`이 바뀌면 응답 메시지가 바뀜
- `Locale` 파라미터가 자동 주입됨

## 11. 자주 헷갈리는 질문

### Q1. `@RequestParam`과 `@PathVariable`을 같이 쓸 수 있는가?

가능하다. 같은 메서드에서 둘 다 받을 수 있다.

### Q2. 커맨드 객체에서 `@ModelAttribute`는 항상 붙여야 하는가?

아니다. 매개변수에 `@ModelAttribute`를 생략해도 객체 자동 바인딩은 동작한다.
다만 모델 키를 지정하고 싶다면 `@ModelAttribute("name")`을 붙인다.

### Q3. `RedirectAttributes.addAttribute`와 `addFlashAttribute`는?

- `addAttribute`: 쿼리스트링으로 붙는다 (`?key=value`)
- `addFlashAttribute`: 세션에 잠깐 저장되었다가 한 번 사용 후 사라진다

### Q4. `addViewControllers`로 동일 경로를 두 번 등록하면?

마지막 등록이 우선되며, 컨트롤러 메서드가 같은 경로에 있다면 일반적으로 컨트롤러 매핑이 우선된다.
이 부분은 학습용으로 의도적으로 충돌을 만들지 말 것.

### Q5. `messageSource.getMessage`에서 키가 없으면?

`NoSuchMessageException`이 발생한다.
필요하다면 `getMessage(key, args, defaultMessage, locale)` 형태로 기본값을 줄 수 있다.

### Q6. `Locale.KOREAN`과 `Locale.KOREA`는?

- `Locale.KOREAN`: 언어만 (`ko`)
- `Locale.KOREA`: 언어+지역 (`ko_KR`)

`messages_ko.properties` 파일은 `ko`, `ko_KR` 둘 다에 매핑된다.

### Q7. 폼 한글이 깨지면?

- JSP의 `pageEncoding`을 UTF-8로 통일
- `application.properties`에서 `spring.servlet.encoding.charset=UTF-8`도 가능 (Spring Boot 기본값)
- `ResourceBundleMessageSource.setDefaultEncoding("UTF-8")` 설정 필수

## 12. 시험 대비 핵심 정리

- 요청 매핑 어노테이션은 `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`이다.
- 클래스 레벨에 `@RequestMapping`을 두면 공통 경로 prefix가 된다.
- `@RequestParam`은 쿼리스트링/폼 단일 값, `@PathVariable`은 URL 경로 변수다.
- `@RequestParam`은 기본적으로 필수다. `required=false`나 `defaultValue`로 옵션 처리한다.
- 컨트롤러 메서드는 `Model`, `ModelAndView`, `RedirectAttributes`, `Locale`, `HttpServletRequest` 등을 받을 수 있다.
- 커맨드 객체는 폼 데이터를 자바 빈 객체로 자동 바인딩한 결과다.
- 폼의 `name` 속성이 자바 빈의 setter와 매칭되어야 자동 바인딩이 동작한다.
- JSP에서 커맨드 객체에 접근할 때 기본 키는 클래스 이름의 카멜케이스다.
- `@ModelAttribute`를 메서드에 붙이면 같은 컨트롤러 안의 모든 요청 처리 메서드 전에 실행된다.
- `@ModelAttribute`를 매개 변수에 붙이면 자동 바인딩하면서 모델 키를 명시할 수 있다.
- 리스트 바인딩은 `name="prop[0]"` 형식을 사용한다.
- 중첩 객체 바인딩은 `name="parent.child"` 형식을 사용한다.
- `redirect:` 접두어로 리다이렉트하며, 일시적 데이터는 `RedirectAttributes.addFlashAttribute`로 전달한다.
- 컨트롤러 메서드가 필요 없는 단순 경로는 `WebMvcConfigurer.addViewControllers`로 매핑한다.
- 메시지 다국화는 `MessageSource`, `LocaleResolver`, `LocaleChangeInterceptor`로 구성된다.
- `messages.properties`(기본), `messages_ko.properties`, `messages_en.properties` 파일은 같은 `basename`을 가져야 한다.
- `Locale` 파라미터는 컨트롤러 메서드에 자동 주입된다.
