# Week 07

## 주제

7주차는 `AOP(Aspect Oriented Programming)`를 배우는 주차다.

AOP는 핵심 기능 코드와 공통 기능 코드를 분리해서,
여러 서비스 메서드에 반복되는 처리를 한 곳에서 관리하게 해 주는 방식이다.

대표적인 공통 기능:

- 로깅
- 인증 및 권한 확인
- 실행 시간 측정
- 예외 기록
- 트랜잭션 관리

## 이 주차가 왜 중요한가

3~4주차에서 DI를 배우면서 객체 사이의 의존을 분리했다.
5~6주차에서는 스프링이 빈을 어떻게 만들고 관리하는지 배웠다.

7주차는 그 다음 단계다.

이제 관심은 다음 질문으로 넘어간다.

- 여러 메서드에 반복되는 코드를 어떻게 한 곳으로 모을 수 있는가?
- 서비스 메서드 실행 전후에 공통 처리를 어떻게 끼워 넣는가?
- 핵심 로직을 수정하지 않고 인증, 로그, 예외 처리를 적용할 수 있는가?

즉 7주차는 스프링이 빈을 관리하는 수준을 넘어,
빈의 메서드 실행 흐름에 공통 기능을 적용하는 방법을 배운다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [4주차](../week04/README.md) DI
- [5주차](../week05/README.md) 빈 객체 관리
- [6주차](../week06/README.md) 빈 객체 관리 심화 실습

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 핵심 바로가기

- [AOP 주요 구성 요소](theory.md#3-aop-주요-구성-요소)
- [Advice 종류](theory.md#4-advice-종류)
- [Pointcut 표현식](theory.md#5-pointcut-표현식)
- [Spring AOP의 동작 방식](theory.md#9-spring-aop의-동작-방식)
- [Before Advice 실습](practice.md#2-before-advice-실습)
- [After Advice 실습](practice.md#3-after-advice-실습)
- [Around Advice 실습](practice.md#4-around-advice-실습)
- [Annotation Pointcut 실습](practice.md#5-annotation-pointcut-실습)

## 이 주차에서 꼭 잡아야 하는 질문

- AOP는 왜 필요한가?
- 핵심 관심사와 공통 관심사는 어떻게 다른가?
- Advice, JoinPoint, Pointcut, Aspect는 각각 무엇인가?
- `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around`는 언제 실행되는가?
- `JoinPoint`와 `ProceedingJoinPoint`는 어떤 정보를 제공하는가?
- Pointcut 표현식 `execution`, `within`, `args`, `@annotation`은 어떻게 다른가?
- Spring AOP가 지원하는 JoinPoint의 한계는 무엇인가?

## 추천 읽기 순서

1. `theory.md`에서 AOP 용어와 Advice 종류 정리
2. Pointcut 표현식 예제 확인
3. Spring AOP의 프록시 기반 동작 방식 이해
4. `practice.md`에서 `/week07` 실습 화면과 코드 연결
5. `/week07` 라우트에서 Advice 실행 순서 확인

## 빠른 요약

- AOP는 공통 기능을 핵심 로직에서 분리하는 프로그래밍 방식이다.
- Spring AOP는 주로 스프링 빈의 메서드 실행 시점을 가로챈다.
- Advice는 실행할 공통 기능이고, Pointcut은 적용 대상을 고르는 규칙이다.
- Aspect는 Advice와 Pointcut을 묶은 클래스다.
- `@Around`는 대상 메서드 실행 자체를 감싸므로 가장 강력하지만, `proceed()` 호출을 직접 관리해야 한다.
