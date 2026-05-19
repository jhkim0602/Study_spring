# Week 11 Practice

## 주제

11주차 실습은 제공된 `BasicProcessController1`, `MvcConfig`, `RegisterRequest`, `Question`, `AnsweredData`, `Respondent` 흐름을 현재 프로젝트 구조에 맞게 재작성한 것이다.

원본 실습은 강의자료의 Ex1~Ex6 흐름을 따라간다.
- Ex1: 경로 매핑, `@PathVariable`, `RedirectAttributes`
- Ex2: 컨트롤러 없이 ViewController로 뷰 매핑
- Ex3: 커맨드 객체 자동 바인딩 (주석으로 보존)
- Ex4: `@ModelAttribute`로 초기값 주입
- Ex5: 리스트/중첩 객체 커맨드
- Ex6: 메시지 다국화

## 이 실습의 목적

11주차 실습의 목표는 "한 컨트롤러가 받을 수 있는 모든 종류의 입력 패턴"을 한 번에 확인하는 것이다.

- `@PathVariable`로 경로 변수를 받기
- `@RequestParam`으로 단일 값을 받기
- 자바 빈 자동 바인딩
- `@ModelAttribute`로 모델 키 명시와 초기화
- 리스트와 중첩 객체 바인딩
- `RedirectAttributes`로 flash 메시지 전달
- 컨트롤러 없이 단순 경로 매핑
- `MessageSource`로 다국어 메시지 조회

## 관련 파일

| 경로 | 역할 |
|---|---|
| [`src/main/java/Lect_B/week11/BasicProcessController1.java`](../../src/main/java/Lect_B/week11/BasicProcessController1.java) | `/regist/...` 라우트 제어 |
| [`src/main/java/Lect_B/week11/MvcConfig.java`](../../src/main/java/Lect_B/week11/MvcConfig.java) | ViewController, MessageSource, LocaleResolver, LocaleChangeInterceptor 등록 |
| [`src/main/java/Lect_B/week11/RegisterRequest.java`](../../src/main/java/Lect_B/week11/RegisterRequest.java) | 회원 가입 커맨드 객체 |
| [`src/main/java/Lect_B/week11/Question.java`](../../src/main/java/Lect_B/week11/Question.java) | 설문 문항 (Lombok `@RequiredArgsConstructor`) |
| [`src/main/java/Lect_B/week11/AnsweredData.java`](../../src/main/java/Lect_B/week11/AnsweredData.java) | 설문 응답 커맨드 객체 (리스트+중첩) |
| [`src/main/java/Lect_B/week11/Respondent.java`](../../src/main/java/Lect_B/week11/Respondent.java) | 설문 응답자 (중첩 객체) |
| [`src/main/java/Lect_B/week11/Week11IndexController.java`](../../src/main/java/Lect_B/week11/Week11IndexController.java) | `/week11` 진입 |
| [`src/main/resources/messages.properties`](../../src/main/resources/messages.properties) | 기본 메시지 |
| [`src/main/resources/messages_ko.properties`](../../src/main/resources/messages_ko.properties) | 한국어 메시지 |
| [`src/main/resources/messages_en.properties`](../../src/main/resources/messages_en.properties) | 영어 메시지 |
| [`src/main/webapp/views/week11/*.jsp`](../../src/main/webapp/views/week11) | 실습 결과 화면 |
| [`src/test/java/Lect_B/week11/Week11ContextTests.java`](../../src/test/java/Lect_B/week11/Week11ContextTests.java) | MessageSource, 커맨드 객체 테스트 |

원본 11주차 실습 파일과 현재 프로젝트의 대응 관계는 다음과 같다.

| 원본 실습 파일 | 원본 의도 | 현재 프로젝트 반영 |
|---|---|---|
| `BasicProcessController1.java` (`com.week11`) | Ex1~Ex6 통합 컨트롤러 | `Lect_B.week11.BasicProcessController1` |
| `MvcConfig.java` | ViewController, MessageSource, LocaleResolver, LocaleChangeInterceptor | 동일 + 패키지만 변경 + `/week11/localeResolver` 매핑 추가 |
| `RegisterRequest.java` | Lombok 기반 커맨드 객체 | 동일 |
| `Question.java`, `AnsweredData.java`, `Respondent.java` | 리스트/중첩 객체 바인딩 예제 | 동일 |
| `view/registerStep1.jsp` ~ `welcome.jsp` | 폼/결과/환영 화면 | `views/week11/`로 이동, 일부 JSP 인코딩과 깨진 한글 정리 |
| `resources/messages*.properties` | 메시지 다국화 파일 | `src/main/resources/`로 이동 |

## 1. 왜 `week11` 전용 패키지를 따로 만들었는가

11주차는 다음과 같은 새로운 기능을 한꺼번에 도입한다.

- 클래스 레벨 `@RequestMapping`
- 리스트/중첩 객체 바인딩
- 메시지 다국화 + LocaleResolver

기존 주차의 컨트롤러와 빈 이름이 겹치지 않도록 새 패키지를 만들었다.

```text
src/main/java/Lect_B/week11/
src/main/resources/messages.properties
src/main/resources/messages_ko.properties
src/main/resources/messages_en.properties
src/main/webapp/views/week11/
docs/week11/
```

## 2. `BasicProcessController1`의 라우트 한 줄씩 읽기

### 2-1. Ex1 - `/regist/step/{id}`

```java
@GetMapping("/step/{id}")
public String detail(@PathVariable("id") Long stepId) {
    String viewName = "registerStep" + stepId;
    return "week11/" + viewName;
}
```

- URL의 `{id}` 자리에 들어온 숫자를 `stepId`로 받는다
- 뷰 이름을 `week11/registerStep1`, `week11/registerStep2`, `week11/registerStep3` 중 하나로 결정

테스트:

- `/regist/step/1` → `registerStep1.jsp` (약관 동의 폼)
- `/regist/step/2` → `registerStep2.jsp` (회원 정보 입력 폼)
- `/regist/step/3` → `registerStep3.jsp` (가입 완료 화면)

### 2-2. Ex1 - POST `/regist/step2`

```java
@PostMapping("/step2")
public String handleStep2(
        @RequestParam("view") String view,
        @RequestParam(value = "agree", defaultValue = "false") Boolean agree,
        RedirectAttributes redirectAttributes) {
    if (!agree) {
        redirectAttributes.addFlashAttribute("message", "약관 동의를 해주세요.");
        return "redirect:/regist/step/1";
    }
    return "week11/" + view;
}
```

- 폼 데이터 `view`, `agree`를 받는다
- 약관 미동의 시 `addFlashAttribute`로 메시지 전달 후 step1로 리다이렉트
- 약관 동의 시 `registerStep2.jsp`로 forward

### 2-3. Ex1 - POST `/regist/step3`

```java
@PostMapping("/step3")
public String handleStep3(
        @RequestParam("view") String view,
        @RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        Model model) {
    RegisterRequest registerRequest = new RegisterRequest(email, name, password, confirmPassword);
    model.addAttribute("registerRequest", registerRequest);
    return "week11/" + view;
}
```

- 모든 폼 필드를 `@RequestParam`으로 일일이 받는 방식
- 강의자료 Ex3/Ex4부터는 다음 형태로 자동 바인딩하는 것이 보통이다. (원본 코드의 주석 참고)

```java
@PostMapping("/step3")
public String handleStep3(RegisterRequest registerRequest, Model model, @RequestParam("view") String view) {
    return "week11/" + view;
}
```

학습 흐름:

1. 먼저 명시적 방식으로 어떤 값을 받는지 이해
2. 그 다음 커맨드 객체 자동 바인딩으로 어떻게 줄어드는지 비교

### 2-4. Ex4 - `@ModelAttribute` 초기화

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

@GetMapping("/initCommand")
public String initForm() {
    return "week11/registerStep2";
}
```

- `initCommand`는 같은 컨트롤러의 모든 요청 처리 메서드 실행 전에 호출
- 반환된 `RegisterRequest`가 모델에 `registerRequest`로 자동 추가
- step2 폼이 placeholder 텍스트로 채워짐

테스트: `/regist/initCommand` 진입 → 폼이 빈 칸이 아니라 placeholder 텍스트로 시작

### 2-5. Ex5 - 설문 폼

```java
@GetMapping("/survey")
public String form(Model model) {
    Question q1 = new Question("당신의 역할은 무엇입니까?", Arrays.asList("서버", "프론트", "풀스택"));
    Question q2 = new Question("많이 사용하는 개발도구는 무엇입니까?", Arrays.asList("이클립스", "인텔리J", "서브라임"));
    Question q3 = new Question("하고 싶은 말을 적어주세요.");
    model.addAttribute("questions", Arrays.asList(q1, q2, q3));
    return "week11/surveyForm";
}

@PostMapping("/survey")
public String submit(@ModelAttribute("ansData") AnsweredData data) {
    return "week11/surveySubmitted";
}
```

- `Question`은 `@RequiredArgsConstructor`로 `title`만 받는 생성자도 자동 생성
- `q3`처럼 옵션 없는 문항은 텍스트 입력으로 표시
- 폼 제출 시 `responses[0]`, `responses[1]`, `responses[2]`, `res.location`, `res.age`가 `AnsweredData`로 자동 바인딩

테스트: `/regist/survey` → 라디오/텍스트 입력 → POST → 결과 화면에서 응답 목록과 응답자 정보 출력

### 2-6. Ex6 - 메시지 다국화

```java
@Autowired
private MessageSource messageSource;

@GetMapping("/message")
public String message(Locale locale, Model model) {
    String greeting = messageSource.getMessage("greeting", null, locale);
    String farewell = messageSource.getMessage("farewell", null, locale);
    model.addAttribute("greeting", greeting);
    model.addAttribute("farewell", farewell);
    return "week11/greeting";
}
```

흐름:

1. 사용자가 `/regist/message?lang=en` 호출
2. `LocaleChangeInterceptor`가 `?lang=en`을 보고 `LocaleResolver`에 영어 저장
3. `Locale locale` 파라미터에 영어 로케일 주입
4. `messageSource.getMessage("greeting", null, Locale.ENGLISH)` → `messages_en.properties`의 `greeting` 조회
5. JSP에 결과 출력

테스트:

- `/regist/message?lang=ko` → "안녕하세요.hello", "수고하셨습니다. 안녕히 가세요."
- `/regist/message?lang=en` → "Hello", "Goodbye"

## 3. `MvcConfig` 한 줄씩 읽기

```java
@Override
public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/main").setViewName("week11/welcome");
    registry.addViewController("/week11/localeResolver").setViewName("week11/localeResolver");
}
```

- `/main`: 강의자료 Ex2의 환영 화면
- `/week11/localeResolver`: 한/영 선택 링크 페이지 (`localeResolver.jsp`)

```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
    ms.setBasename("messages");
    ms.setDefaultEncoding("UTF-8");
    return ms;
}
```

- `basename`을 `messages`로 두면 `messages.properties`, `messages_ko.properties`, `messages_en.properties`를 자동 매핑

```java
@Bean
public SessionLocaleResolver localeResolver() {
    SessionLocaleResolver resolver = new SessionLocaleResolver();
    resolver.setDefaultLocale(Locale.KOREAN);
    return resolver;
}

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

- `SessionLocaleResolver`: 세션 단위로 로케일 보관
- `LocaleChangeInterceptor`: 쿼리스트링 `lang` 값으로 자동 변경
- `addInterceptors`로 인터셉터를 실제 적용

## 4. JSP 화면이 무엇을 보여 주는가

| JSP | 역할 |
|---|---|
| `views/week11/index.jsp` | 11주차 실습 메인 메뉴 |
| `views/week11/welcome.jsp` | `/main` 환영 화면, Ex2 |
| `views/week11/registerStep1.jsp` | 약관 동의 폼, Ex1 |
| `views/week11/registerStep2.jsp` | 회원 정보 입력 폼, Ex1/Ex4 |
| `views/week11/registerStep3.jsp` | 가입 완료 화면, Ex1 |
| `views/week11/surveyForm.jsp` | 설문조사 폼, Ex5 |
| `views/week11/surveySubmitted.jsp` | 설문 응답 결과, Ex5 |
| `views/week11/localeResolver.jsp` | 한/영 언어 선택, Ex6 |
| `views/week11/greeting.jsp` | 다국화 메시지 결과, Ex6 |

JSP에서 자주 등장하는 패턴:

- `${registerRequest.email}`: 커맨드 객체 접근
- `${q.choice}`: `Question.isChoice()` 호출 (EL의 boolean getter 규칙)
- `responses[${status.index}]`: 리스트 바인딩용 인덱스 name
- `res.location`, `res.age`: 중첩 객체 바인딩
- `${message}`: flash 메시지

## 5. 실행 결과 보는 법

### 회원가입 흐름

1. `/week11` → 11주차 메뉴
2. `/regist/step/1` → 약관 동의 폼
3. 약관 미동의 후 제출 → step/1로 돌아오면서 flash 메시지 표시
4. 약관 동의 후 제출 → `registerStep2.jsp` 폼 표시
5. 폼 채우고 제출 → `registerStep3.jsp` 가입 완료 화면

### `@ModelAttribute` placeholder 흐름

1. `/regist/initCommand` → step2 폼에 placeholder 채워진 상태
2. 입력 후 제출 → 일반 흐름과 동일

### 컨트롤러 없는 매핑

1. `/main` → `welcome.jsp` (컨트롤러 없이 ViewController로 매핑)

### 설문조사 흐름

1. `/regist/survey` → 3문항 폼 (q3은 텍스트 입력)
2. 응답 입력 후 제출 → `surveySubmitted.jsp`에 응답 목록 + 응답자 위치/나이 표시

### 다국화 흐름

1. `/week11/localeResolver` → 한/영 링크 페이지
2. `/regist/message?lang=ko` → 한국어 인사
3. `/regist/message?lang=en` → 영어 인사

## 6. 강의자료와의 차이점 정리

- 패키지명 `com.week11` → `Lect_B.week11`
- JSP의 `pageEncoding` 통일 (UTF-8)
- `welcome.jsp`의 깨진 한글(EUC-KR)을 UTF-8로 정리
- `/regist/step2` 리다이렉트 URL에서 `http://localhost:8080` 절대 경로 → `/regist/step/1` 상대 경로 (포트 변경 시에도 동작)
- `MvcConfig`에 `/week11/localeResolver` 뷰 매핑 추가 (다국어 진입점)
- `BasicProcessController1`을 `Lect_B.week11.BasicProcessController1`로 이동 후 `Lect8Application.scanBasePackages`에 `Lect_B.week11` 추가
- `addInterceptors`에서 12주차 학습용 `AuthCheckInterceptor` 부분은 주석 그대로 유지 (강의자료 학습 흐름 보존)

## 7. 테스트로 확인하기

`Week11ContextTests`는 다음을 자동으로 검증한다.

| 테스트 | 확인 내용 |
|---|---|
| `week11BeansAreLoaded` | `BasicProcessController1`, `MessageSource` 빈 등록 |
| `messageSourceResolvesKoreanGreeting` | `messages_ko.properties`의 `greeting` 해석 |
| `messageSourceResolvesEnglishGreeting` | `messages_en.properties`의 `greeting` 해석 |
| `initCommandSetsPlaceholders` | `@ModelAttribute` 초기화 placeholder 값 |
| `registerRequestPasswordMatch` | `isPasswordEqualToConfirmPassword()` 동작 |
| `questionIsChoiceFlagReflectsOptions` | 옵션 유무에 따른 `isChoice()` 결과 |

## 8. 12주차 준비

`MvcConfig`에는 12주차 학습용으로 다음 라인이 주석으로 보존되어 있다.

```java
/*
@Autowired
private AuthCheckInterceptor authCheckInterceptor;

@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authCheckInterceptor)
        .addPathPatterns("/changePassword")
        .excludePathPatterns("/edit/help/*");
}

@Override
public Validator getValidator() {
    return new RegisterRequestValidator();
}
*/
```

12주차에서 `Validator`와 인터셉터를 실습할 때 이 주석을 풀어 비교하면 된다.
