# Week 04

## 주제

4주차는 **DI를 실제 코드에서 어떻게 다루는가**를 배우는 주차다.  
3주차가 "빈과 컨테이너의 존재를 이해하는 단계"였다면, 4주차는 "그 빈을 실제로 주입하고 구분하고 사용해 보는 단계"다.

## 이 주차가 왜 중요한가

실무에서 스프링을 쓴다고 할 때 가장 자주 보는 문법이 바로:

- `@Autowired`
- `@Qualifier`
- `@Value`
- `@Configuration`
- `@Bean`

이다.

즉 4주차는 "스프링다운 코드"가 어떤 코드인지 처음 체감하는 주차라고 볼 수 있다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [3주차](../week03/README.md)의 Bean / DI / 컨테이너 개념

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)
- [연결 과제 문서](../assignment2223002/README.md)

## 이 주차에서 꼭 잡아야 하는 질문

- DI 방식은 왜 여러 가지가 있는가?
- `@Autowired`는 어떤 기준으로 빈을 찾는가?
- 같은 타입 빈이 여러 개면 왜 `@Qualifier`가 필요한가?
- 설정값은 왜 `application.properties` 밖으로 빼는가?
- 컬렉션도 왜 빈으로 주입할 수 있는가?

## 추천 읽기 순서

1. `theory.md`에서 DI 방식과 어노테이션 정리
2. `practice.md`에서 `/week04` 실습 구조 확인
3. `HardWorkUnit`, `LombokWorkUnit`, `DIController` 코드 읽기

## 빠른 요약

- 4주차는 DI를 "정의"가 아니라 "사용법"으로 배우는 주차다.
- 같은 타입 빈이 여러 개일 때의 구분법까지 포함해 실전적인 DI를 본다.
- Java Config, XML, 어노테이션 방식을 한 프로젝트 안에서 비교한다.

## 연결 과제

- [과제 이론 문서](../assignment2223002/theory.md)
  - 리스트 DI, 학생 수 `@Value`, Java Config 설계가 실제 과제에서 어떻게 쓰였는지 연결해 볼 수 있다.
