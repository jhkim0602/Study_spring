# Week 04

## 주제

4주차는 DI(Dependency Injection)를 본격적으로 다루는 주차다.  
강의자료에서는 스프링 컨테이너, Bean 설정 방식, DI 방식, `@Autowired`, `@Qualifier`, `@Value`, Java Config, XML 설정, 컬렉션 주입까지 넓게 다루고,  
실습에서는 `Lect_B.week04` 패키지에서 Java Config와 어노테이션 기반 DI를 웹 요청으로 확인하는 흐름을 구성했다.

## 문서 구성

- [실습 문서](practice.md)
- [이론 문서](theory.md)

## 빠른 요약

- 3주차가 "Bean과 컨테이너가 무엇인가"에 가까웠다면, 4주차는 "DI를 실제로 어떻게 쓰는가"에 가깝다.
- 강의자료 기준 핵심 범위는 Bean 설정 3방식, DI 3방식, `@Autowired`, `@Qualifier`, `@Value`, Java Config, XML 주입, 컬렉션 주입이다.
- 현재 프로젝트 실습은 `AppConfig`, `DIController`, `HardWorkUnit`, `WorkUnit`, `SmsSender`, JSP 화면을 중심으로 구성되어 있다.
- 실습 라우트는 `/annotationDI`, `/configDI`이며, 각각 어노테이션 기반 DI와 Java Config 기반 Bean 조회를 확인한다.

## 이번 주차를 보는 관점

4주차 문서는 "새로 만든 최종본"만 기록하는 문서가 아니라,  
강의에서 배운 이론과 그 이론을 바탕으로 실습하면서 쌓인 내용을 함께 누적하는 문서다.

즉 아래 두 축을 같이 본다.

- 강의자료에서 배운 DI 이론 정리
- 현재 프로젝트에서 직접 확인한 실습 정리
