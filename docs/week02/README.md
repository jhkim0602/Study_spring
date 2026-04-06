# Week 02

## 주제

2주차는 **웹 요청이 어떻게 화면으로 이어지는가**를 배우는 시작점이다.  
아직 Bean, DI, Scope를 몰라도 된다. 먼저 "브라우저에서 URL을 입력하면 서버에서 무슨 일이 일어나는가"를 이해하는 것이 목표다.

## 이 주차가 왜 중요한가

이후 주차에서 계속 나오는:

- Controller
- Model
- View
- JSP
- View Resolver
- URL 매핑

같은 단어가 모두 2주차에서 출발한다.

즉 2주차를 모르면:

- 왜 컨트롤러가 필요한지
- 왜 JSP 파일을 `views/` 아래 두는지
- 왜 `ModelAndView`를 쓰는지

가 계속 흐릿하게 남는다.

## 먼저 알고 가면 좋은 선수 개념

- [용어 사전](../glossary.md)
- [공통 기초 문서](../foundation.md)
- [프로젝트 구조 해설](../project-structure.md)
- 자바의 `class`, `object`, `method`
- 웹의 `request`, `response`

## 문서 구성

- [이론 문서](theory.md)
- [실습 문서](practice.md)

## 추천 읽기 순서

1. `theory.md`에서 MVC와 JSP 개념 파악
2. `practice.md`에서 현재 프로젝트 파일 구조 확인
3. 실제 `src/main/webapp` 폴더 열어보기

## 이 주차를 끝내면 이해해야 하는 질문

- JSP는 무엇인가?
- URL 요청은 누가 받는가?
- Controller는 왜 필요한가?
- View Resolver는 뭘 하는가?
- JSP 파일이 있다고 해서 왜 URL로 바로 열리지 않는가?

## 빠른 요약

- JSP는 화면 파일이다.
- Controller는 요청을 처리하는 자바 클래스다.
- View Resolver는 뷰 이름을 실제 JSP 경로로 바꾼다.
- 2주차는 스프링 웹 프로젝트의 "입구 구조"를 이해하는 단계다.
