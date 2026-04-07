# Week 06

## 주제

6주차는 5주차에서 배운 `빈 객체 관리`를  
**교수님 실습 코드 흐름 중심으로 다시 한 번 확인하는 심화 주차**다.

핵심 파일:

- `BeanScopeConfig`
- `BeanScopeController`
- `InitDestroyUnit`
- `AwareInterfaceImp`
- `ExternalConfigComponent`

## 이 주차가 왜 중요한가

5주차에서 개념을 한 번 정리했다면, 6주차는 그 개념을 "실습용 코드 패턴"으로 다시 읽게 한다.

즉 6주차의 역할은:

- 개념을 반복해서 굳히고
- 스코프, 라이프사이클, 외부 설정을
- 실제 컨트롤러-뷰 흐름 안에서 확인하는 것

이다.

## 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [5주차](../week05/README.md) 내용

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)
- [연결 과제 문서](../assignment2223002/README.md)

## 이 주차에서 꼭 잡아야 하는 질문

- 스코프 차이를 실제 출력으로 어떻게 확인하는가?
- 같은 prototype이 재사용되는 문제를 화면에서 어떻게 증명하는가?
- 라이프사이클 훅의 실행 순서를 어떻게 검증하는가?
- Aware 인터페이스는 실제로 어떤 데이터를 주는가?
- 외부 프로퍼티가 화면에 출력되는 구조는 어떻게 되는가?

## 추천 읽기 순서

1. `theory.md`에서 개념 다시 정리
2. `practice.md`에서 원본 실습과 현재 프로젝트 구조 연결
3. `/week06` 라우트 하나씩 실행해 보기

## 빠른 요약

- 6주차는 5주차 개념의 반복이 아니라, 실습 중심 재확인 단계다.
- 같은 개념을 다른 코드 형태로 다시 보면 이해가 훨씬 단단해진다.

## 연결 과제

- [과제 이론 문서](../assignment2223002/theory.md)
  - `ApplicationContextAware`, `InitializingBean`, 실행 흐름이 실제 과제에서 어떻게 쓰였는지 바로 연결해서 볼 수 있다.
