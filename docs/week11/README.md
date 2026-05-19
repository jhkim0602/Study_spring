# Week 11

## 주제

11주차는 **요청 매핑 · 커맨드 객체 · 메시지 다국화**를 배우는 주차다.

지금까지(2~10주차)는 빈, DI, AOP, JDBC 같은 컨테이너/데이터 영역을 다뤘다면,
11주차는 다시 **컨트롤러와 폼 입력 처리**로 돌아간다.
하지만 단순한 hello world가 아니라 회원 가입처럼 여러 단계와 여러 종류의 파라미터, 여러 언어를 처리해야 하는 실제 패턴이다.

핵심 파일:

- `BasicProcessController1`
- `MvcConfig`
- `RegisterRequest`
- `Question`, `AnsweredData`, `Respondent`
- `messages.properties`, `messages_ko.properties`, `messages_en.properties`

## 이 주차가 왜 중요한가

지금까지 컨트롤러는 주로 단순 GET 요청 한 두 개만 처리했다.
하지만 실제 폼 처리는 다음을 동시에 다뤄야 한다.

- 여러 경로(`/regist/step/1`, `/regist/step2`, `/regist/step3`)
- GET과 POST 구분
- `@PathVariable`, `@RequestParam`로 단순 값 전달
- 자바 빈을 통째로 받는 커맨드 객체 (`@ModelAttribute`)
- 리스트와 중첩 객체를 가진 커맨드 객체
- 폼 검증 실패 시 리다이렉트와 한 번만 살아남는 flash 데이터 (`RedirectAttributes`)
- 컨트롤러 없이 단순 경로 → 뷰 매핑 (`addViewControllers`)
- 메시지 다국화 (`MessageSource`, `LocaleResolver`, `LocaleChangeInterceptor`)

즉 11주차의 역할은:

- 강의자료의 Ex1~Ex6 흐름을 따라가며
- 컨트롤러 메서드에서 받을 수 있는 **파라미터 종류**와 **반환 형태**를 구분하고
- 화면과 컨트롤러 사이의 데이터 흐름을 직접 확인하는 것

이다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [2주차](../week02/README.md)의 컨트롤러/JSP 기본
- [4주차](../week04/README.md)의 DI

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 이 주차에서 꼭 잡아야 하는 질문

- `@RequestMapping`, `@GetMapping`, `@PostMapping`은 어떻게 다른가?
- `@PathVariable`과 `@RequestParam`은 무엇이 다른가?
- 컨트롤러 메서드가 받을 수 있는 파라미터 종류는?
- 커맨드 객체란 무엇이며, 어떻게 자동 바인딩되는가?
- `@ModelAttribute`는 매개 변수에 붙을 때와 메서드에 붙을 때 무엇이 다른가?
- 리스트와 중첩 객체를 가진 커맨드 객체는 폼에서 어떤 `name` 규칙으로 작성하는가?
- `RedirectAttributes.addFlashAttribute`는 일반 `model.addAttribute`와 무엇이 다른가?
- `addViewControllers`는 왜 컨트롤러 클래스를 만들 필요가 없게 해 주는가?
- `MessageSource`, `LocaleResolver`, `LocaleChangeInterceptor`는 어떻게 협업하는가?

## 추천 읽기 순서

1. `theory.md`에서 컨트롤러 파라미터 종류와 커맨드 객체 개념 정리
2. `RegisterRequest`, `Question`, `AnsweredData`, `Respondent` 코드 확인
3. `BasicProcessController1`의 각 `@GetMapping` / `@PostMapping` 메서드를 강의자료의 Ex1~Ex6 순서대로 읽기
4. `MvcConfig`에서 `addViewControllers`, `messageSource`, `localeResolver`, `localeChangeInterceptor` 빈 등록 확인
5. `messages*.properties` 3개 파일 비교
6. `/week11` 라우트를 따라 회원가입 흐름과 설문조사 흐름, 다국어 메시지 흐름 확인
7. `practice.md`로 각 라우트의 결과 확인

## 빠른 요약

- 11주차는 컨트롤러에서 받을 수 있는 파라미터 종류와 폼 처리 패턴을 한 번에 정리하는 주차다.
- `@PathVariable`, `@RequestParam`, 커맨드 객체(@ModelAttribute), 리스트/중첩 커맨드 객체, `RedirectAttributes`, `addViewControllers`, `MessageSource`까지 6가지 핵심 사용 패턴을 다룬다.
- 모든 흐름은 `/regist/step/1` → step2 → step3 → /main 의 회원가입 시나리오와 `/regist/survey` 설문 시나리오, `/regist/message` 다국어 시나리오 하나로 묶여 있다.

## 이전 주차 연결

- [2주차](../week02/README.md)의 컨트롤러/뷰 기본 위에 폼 처리를 확장한다.
- [10주차](../week10/README.md)의 데이터베이스 연동과 함께 보면, 폼 → 커맨드 객체 → Repository로 이어지는 흐름이 완성된다.
