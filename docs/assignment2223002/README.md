# Assignment 2223002

## 주제

이 문서는 `과제 #1`을 기준으로 만든 **과제 전용 학습 문서**다.  
단순 제출용 정리가 아니라, 이 과제를 보고:

- 요구사항을 어떻게 해석해야 하는지
- 어떤 클래스가 왜 필요한지
- 어떤 스프링 개념이 어느 코드에 들어가는지
- 시험에서 비슷한 문제가 나오면 어떻게 풀어야 하는지

까지 연결해서 이해할 수 있게 만드는 것이 목적이다.

## 이 과제가 왜 중요한가

이 과제는 작은 콘솔 프로그램처럼 보이지만,
실제로는 3주차부터 6주차까지 배운 개념을 한 번에 묶는 통합 과제다.

연결되는 주차:

- [3주차](../week03/README.md): Bean, ApplicationContext, `getBean()`
- [4주차](../week04/README.md): DI, `@Value`, Java Config
- [5주차](../week05/README.md): scope, lifecycle, 외부 설정
- [6주차](../week06/README.md): `ApplicationContextAware`, `InitializingBean`, 실행 흐름 확인

즉 이 과제를 제대로 이해하면 주차별 개념이 따로가 아니라 하나의 구조로 연결된다.

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 함께 보면 좋은 기존 문서

- [문서 메인 인덱스](../README.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- [용어 사전](../glossary.md)

## 추천 읽기 순서

1. 먼저 `theory.md`에서 요구사항을 한 줄씩 해석
2. 그 다음 `practice.md`에서 실제 파일과 코드 연결
3. 마지막으로 `src/main/java/Lect_B/assignment2223002` 직접 읽기

## 이 문서를 끝내면 답할 수 있어야 하는 질문

- 왜 `StdInfo`는 prototype이 되어야 하는가?
- 왜 학생 수를 프로퍼티 파일에 두는가?
- 왜 `StdManager`가 `ApplicationContextAware`를 구현하는가?
- 왜 결과 출력은 `destroyMethod`로 연결했는가?
- 왜 이 과제는 그냥 순수 자바 프로그램이 아니라 스프링 과제로 보아야 하는가?

## 빠른 요약

- 이 과제는 "학생 정보를 입력받아 저장하는 프로그램"이면서 동시에
- "스프링 컨테이너가 객체를 생성, 주입, 초기화, 종료까지 관리하는 흐름"을 보여 주는 과제다.
