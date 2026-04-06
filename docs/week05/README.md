# Week 05

## 주제

5주차는 **빈을 어떻게 등록하느냐**보다  
**등록된 빈을 어떤 범위와 생명주기로 관리하느냐**를 배우는 주차다.

핵심 주제:

- Scope
- 서로 다른 Scope 빈의 의존 처리
- Lifecycle
- Aware 인터페이스
- 외부 설정 프로퍼티

## 이 주차가 왜 중요한가

3~4주차에서 "빈을 만든다, 주입한다"까지 배웠다면,  
5주차에서는 "그 빈이 얼마나 오래 살아야 하는가"와 "언제 어떤 메서드가 실행되는가"까지 생각하게 된다.

즉 5주차는 **스프링이 객체를 운영하는 방식**을 배우는 주차다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [3주차](../week03/README.md)의 Bean / Container 개념
- [4주차](../week04/README.md)의 DI 개념

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 이 주차에서 꼭 잡아야 하는 질문

- singleton과 prototype은 왜 나뉘는가?
- request와 session 스코프는 왜 웹에서만 의미가 큰가?
- prototype을 singleton 안에 주입하면 왜 문제가 생기는가?
- 초기화와 종료 메서드는 왜 필요한가?
- 설정값을 외부 파일로 빼는 이유는 무엇인가?

## 추천 읽기 순서

1. `theory.md`에서 scope와 lifecycle 개념 먼저 이해
2. `practice.md`에서 `/week05` 라우트별 실습 확인
3. `ScopeSingletonClient`, `ScopeFactoryClient`, `Week05ExternalConfigComponent` 읽기

## 빠른 요약

- 5주차는 빈의 "운영 정책"을 배우는 주차다.
- 스코프와 라이프사이클은 실제 서비스 코드에서 매우 중요하다.
- 외부 설정 프로퍼티는 실무적인 설정 관리의 출발점이다.
