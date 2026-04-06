# Week 05

## 주제

5주차는 빈 객체 관리 주차다.  
강의자료 기준으로는 `scope`, 서로 다른 범위 빈의 의존 처리, 라이프사이클 메서드, `Aware` 인터페이스, 외부 설정 프로퍼티를 다룬다.

## 문서 구성

- [실습 문서](practice.md)
- [이론 문서](theory.md)

## 빠른 요약

- singleton, prototype, request, session 스코프의 차이를 이해해야 한다.
- singleton이 prototype을 직접 주입받으면 "한 번만 생성된 prototype"을 계속 쓰게 된다.
- 라이프사이클은 생성만이 아니라 초기화와 종료까지 포함한다.
- `BeanNameAware`, `ApplicationContextAware`는 컨테이너와 직접 상호작용할 때 쓰인다.
- 외부 설정은 `@Value`와 `@ConfigurationProperties`를 상황에 맞게 나눠 쓰는 것이 핵심이다.
