# Week 06

## 주제

6주차는 빈 객체 관리 심화 주차다.  
제공된 실습은 `BeanScopeConfig`, `BeanScopeController`, `InitDestroyUnit`, `AwareInterfaceImp`, `ExternalConfigComponent`를 중심으로 구성되어 있다.

## 문서 구성

- [실습 문서](practice.md)
- [이론 문서](theory.md)

## 빠른 요약

- singleton, prototype, request, session 스코프를 실제 요청에서 비교한다.
- singleton이 prototype을 직접 받으면 같은 prototype이 계속 재사용된다.
- `ObjectFactory`는 필요한 시점에 prototype 빈을 새로 꺼내는 해결책이다.
- 라이프사이클, Aware, 외부 프로퍼티를 한 주차 안에서 이어서 확인한다.
