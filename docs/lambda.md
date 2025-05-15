# 람다 스페셜

## 목차

* 람다 대수 소개
  * 람다 대수의 역사와 개념
  * 람다 대수 기본 개념과 문법
  * 람다 대수의 변환과 계산
  * Java의 람다 표현식
  * 클로저(closure)
* Java 람다 표현식
  * `@FunctionalInterface`
  * 내장 함수 인터페이스(`java.util.function`)
  * 스트림 API와 컬렉션 조작
  * 비동기 처리
  * 동시성 처리


### 데이터 테이블 예제

* STUDENT

|   S_NO   | NAME | ADDRESS |
|:--------:|:----:|:-------:|
| 20231001 | 홍길동  |   강남구   |
| 20231002 | 이순신  |   종로구   |
| 20231003 | 강감찬  |   서초구   |
| 20231004 | 유관순  |   마포구   |

* CLASS

|   C_NO   | CLASS_NAME | TEACHER |
|:--------:|:---------:|:-------:|
| 20231001 |   수학    |   김철수  |
| 20231002 |   과학    |   이영희  |
| 20231003 |   영어    |   박영수  |

* STUDENT_CLASS

|   S_NO   |   C_NO   |
|:--------:|:-------:|
| 20231001 | 20231001 |
| 20231002 | 20231002 |
| 20231003 | 20231001 |
| 20231004 | 20231003 |


```sql
SELECT S.S_NO, S.NAME, S.ADDRESS, C.CLASS_NAME, C.TEACHER
FROM STUDENT S,
     STUDENT_CLASS SC,
     CLASS C
WHERE S.S_NO = SC.S_NO
  AND SC.C_NO = C.C_NO
  AND S.NAME = '홍길동';
```
