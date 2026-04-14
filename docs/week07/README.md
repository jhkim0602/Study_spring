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
- Advice가 언제 실행되는지 이해하고
- Pointcut을 Advice 안에서 함께 읽어 적용 대상을 파악하고
- 실제 컨트롤러-서비스-JSP 흐름 안에서 실행 순서를 확인하는 것

이다.

Pointcut은 별도 암기 단원이 아니라,
**각 Advice가 어느 메서드에 붙는지 설명하는 조건**으로 읽어야 한다.

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
- 각 Advice에 적힌 Pointcut 조건은 어떤 대상 메서드를 가리키는가?
- `@Around`에서 `proceed()`를 호출하지 않으면 어떻게 되는가?

## 추천 읽기 순서

1. `theory.md`에서 AOP가 왜 필요한지 먼저 확인
2. `Week07AopService`에서 핵심 로직만 읽기
3. `Week07AdviceAspect`에서 Advice와 Pointcut 조건을 한 줄씩 같이 읽기
4. `practice.md`에서 `/week07` 라우트별 실행 결과 확인
5. JSP 화면의 Advice 실행 기록과 Aspect 코드를 다시 연결하기

## 빠른 요약

- 7주차는 공통 관심사를 핵심 로직에서 분리하는 주차다.
- Advice는 실행할 공통 기능이고, Pointcut은 그 Advice가 적용될 대상을 고르는 조건이다.
- Pointcut은 독립적으로 외우기보다 `@Before("...")`, `@Around("...")` 안에서 실제 적용 대상을 읽어야 한다.
- Spring AOP는 스프링 빈의 메서드 실행을 프록시로 감싸는 방식으로 동작한다.
- `@Around`는 가장 강력하지만 `proceed()` 호출을 직접 관리해야 한다.

## 이전 주차 연결

- [6주차 이론 문서](../week06/theory.md)
  - 6주차까지 배운 빈 객체 관리 위에 AOP가 적용된다.
