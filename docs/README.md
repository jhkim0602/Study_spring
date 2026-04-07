# lect_B Study Docs

이 폴더는 단순 메모 저장소가 아니라,  
`Spring Boot + JSP + 주차별 실습 코드`를 **처음 배우는 학생도 따라갈 수 있게 다시 설명한 학습 문서 모음**이다.

## 이 문서 폴더를 어떻게 읽으면 좋은가

### 자바 개념도 함께 약한 경우

1. [용어 사전](glossary.md)
2. [공통 기초 문서](foundation.md)
3. [프로젝트 구조 해설](project-structure.md)
4. [2주차](week02/README.md)
5. [3주차](week03/README.md)
6. [4주차](week04/README.md)
7. [5주차](week05/README.md)
8. [6주차](week06/README.md)
9. [과제 문서](assignment2223002/README.md)

### 자바 기본은 알고 있고 스프링이 처음인 경우

1. [공통 기초 문서](foundation.md)
2. [프로젝트 구조 해설](project-structure.md)
3. [2주차](week02/README.md)
4. [3주차](week03/README.md)
5. [4주차](week04/README.md)
6. [5주차](week05/README.md)
7. [6주차](week06/README.md)
8. [과제 문서](assignment2223002/README.md)

### 특정 주차만 빠르게 보고 싶은 경우

1. 먼저 [용어 사전](glossary.md)에서 막히는 단어 확인
2. 해당 주차의 `README.md`
3. 해당 주차의 `theory.md`
4. 필요한 경우 `practice.md`

### 시험 대비용

- 먼저 `README.md`로 큰 흐름 정리
- 다음으로 `theory.md`에서 개념 정리
- 마지막으로 `practice.md`에서 실제 코드와 연결

### 폴더 구조가 먼저 궁금한 경우

1. [프로젝트 구조 해설](project-structure.md)
2. [공통 기초 문서](foundation.md)
3. 필요한 주차 문서

## 문서 운영 원칙

- 각 주차는 `weekXX/` 폴더 단위로 관리한다.
- 공통으로 반복되는 기초 개념은 별도 문서로 분리한다.
- 실습 코드는 `src/`에서 관리하고, 학습 문서는 `docs/`에서 관리한다.
- 문서 설명은 "요약"보다 "이해"를 우선한다.

각 주차 폴더 안에는 아래 3개 파일을 기본으로 둔다.

- `README.md`: 해당 주차의 입구 문서
- `theory.md`: 개념 중심 문서
- `practice.md`: 프로젝트 코드와 연결한 실습 문서

## 폴더를 이렇게 나눈 이유

이 프로젝트는 주차가 바뀔수록 배우는 범위가 조금씩 확장된다.

- 2주차: 웹 요청, JSP, MVC
- 3주차: Bean, 컨테이너, DI 시작
- 4주차: DI 심화
- 5주차: 빈 범위, 라이프사이클, 외부 설정
- 6주차: 5주차 개념을 다시 실습 중심으로 확인

따라서 문서도 주차별로 분리해야:

- 수업 흐름을 보존할 수 있고
- 이전 주차와 다음 주차의 차이를 비교할 수 있고
- 시험 직전에 범위별 복습이 가능하다

## 추천 구조

```text
docs/
  README.md
  glossary.md
  foundation.md
  project-structure.md
  week02/
    README.md
    theory.md
    practice.md
  week03/
    README.md
    theory.md
    practice.md
  week04/
    README.md
    theory.md
    practice.md
  week05/
    README.md
    theory.md
    practice.md
  week06/
    README.md
    theory.md
    practice.md
  assignment2223002/
    README.md
    theory.md
    practice.md
```

## 문서별 역할 차이

### `glossary.md`

수업에서 반복해서 나오는 자바/Spring/MVC 용어를 사전처럼 정리한다.

### `foundation.md`

전체 주차에 공통으로 깔리는 자바/Spring/JSP 기초를 정리한다.

### `project-structure.md`

현재 프로젝트의 실제 폴더 구조와 파일 배치를 해설한다.

### `weekXX/README.md`

해당 주차가 무엇을 배우는지, 왜 중요한지, 어떤 순서로 읽으면 좋은지 알려준다.

### `weekXX/theory.md`

개념을 중심으로 설명한다.

질문 예:

- 빈이 왜 필요한가
- 스프링 컨테이너는 무엇을 하는가
- scope는 왜 필요한가

### `weekXX/practice.md`

현재 프로젝트 코드와 연결해 설명한다.

질문 예:

- 이 파일은 왜 존재하는가
- 이 컨트롤러는 어떤 요청을 받는가
- 이 설정 파일은 어떤 빈을 등록하는가

### `assignment2223002/`

주차별 개념을 실제 과제 문제에 어떻게 적용하는지 정리한 통합 과제 문서다.

## 바로가기

- [용어 사전](glossary.md)
- [공통 기초 문서](foundation.md)
- [프로젝트 구조 해설](project-structure.md)
- [2주차 문서 인덱스](week02/README.md)
- [2주차 이론](week02/theory.md)
- [2주차 실습](week02/practice.md)
- [3주차 문서 인덱스](week03/README.md)
- [3주차 이론](week03/theory.md)
- [3주차 실습](week03/practice.md)
- [4주차 문서 인덱스](week04/README.md)
- [4주차 이론](week04/theory.md)
- [4주차 실습](week04/practice.md)
- [5주차 문서 인덱스](week05/README.md)
- [5주차 이론](week05/theory.md)
- [5주차 실습](week05/practice.md)
- [6주차 문서 인덱스](week06/README.md)
- [6주차 이론](week06/theory.md)
- [6주차 실습](week06/practice.md)
- [과제 문서 인덱스](assignment2223002/README.md)
- [과제 이론](assignment2223002/theory.md)
- [과제 실습](assignment2223002/practice.md)
