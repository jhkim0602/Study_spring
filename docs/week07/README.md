# Week 07

## 주제

7주차는 **AOP(Aspect Oriented Programming)** 를 배우는 주차다.

6주차까지는 스프링이 빈을 만들고, 주입하고, 범위와 생명주기를 관리하는 방법을 봤다.
7주차에서는 그 빈의 **메서드 실행 전후에 공통 기능을 적용하는 방법**을 배운다.

핵심 파일:

- `Week07AopService`
- `Week07AdviceAspect`
- `AopEventLog`
- `TraceAop`
- `Week07AopController`

## 이 주차가 왜 중요한가

서비스 메서드를 작성하다 보면 핵심 로직과 상관없는 코드가 반복된다.

- 로그 기록
- 인증 확인
- 예외 기록
- 실행 시간 측정
- 트랜잭션 처리

이런 코드를 모든 메서드에 직접 넣으면 코드가 지저분해지고,
공통 정책이 바뀔 때 여러 파일을 수정해야 한다.

7주차는 이런 공통 관심사를 핵심 로직에서 분리하는 방법을 배운다.

즉 7주차의 역할은:

- 핵심 관심사와 공통 관심사를 구분하고
- Advice가 실행되는 시점을 이해하고
- Pointcut으로 적용 대상을 고르는 방법을
- 실제 컨트롤러-서비스-JSP 흐름 안에서 확인하는 것

이다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [4주차](../week04/README.md)의 DI 개념
- [5주차](../week05/README.md)의 빈 객체 관리 개념
- [6주차](../week06/README.md)의 빈 객체 관리 심화 실습

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 이 주차에서 꼭 잡아야 하는 질문

- AOP는 왜 필요한가?
- 핵심 관심사와 공통 관심사는 어떻게 다른가?
- Advice, JoinPoint, Pointcut, Aspect는 각각 무엇인가?
- `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around`는 언제 실행되는가?
- Pointcut 표현식 `execution`, `within`, `args`, `@annotation`은 어떻게 다른가?
- `@Around`에서 `proceed()`를 호출하지 않으면 어떻게 되는가?

## 추천 읽기 순서

1. `theory.md`에서 AOP 용어와 Advice 종류 정리
2. `theory.md`의 Pointcut 표현식 부분 확인
3. `practice.md`에서 `/week07` 라우트별 실습 구조 확인
4. `Week07AdviceAspect`, `Week07AopService`, `Week07AopController` 코드 읽기

## 빠른 요약

- 7주차는 공통 관심사를 핵심 로직에서 분리하는 주차다.
- Advice는 실행할 공통 기능이고, Pointcut은 적용 대상을 고르는 규칙이다.
- Spring AOP는 스프링 빈의 메서드 실행을 프록시로 감싸는 방식으로 동작한다.
- `@Around`는 가장 강력하지만 `proceed()` 호출을 직접 관리해야 한다.

## 이전 주차 연결

- [6주차 이론 문서](../week06/theory.md)
  - 6주차까지 배운 빈 객체 관리 위에 AOP가 적용된다.
