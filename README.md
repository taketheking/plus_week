# 플러스 주차 개인 과제



## 목표
Transactional, 인증/인가, N+1, QueryDSL, 테스트 코드에 대해 이해해보자!


## 개발 환경

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">

IDE : Intellij IDEA 2024.2.3

언어 : Java

프레임 워크 : 스프링 부트 3.4.0

JDK : temurin 21.0.3

RDBMS : MYSQL_Server 9.1.0 

버전관리도구 : GitHub

## 요구사항

### **1. Transactional에 대한 이해**

![image](https://github.com/user-attachments/assets/b1071ce5-39ef-4abf-bb64-8f3a77b77efb)


- 설명
    - `createReservation` 함수 40~43번째 유효성 검사 로직이 있습니다. 데이터를 쉽게 생성하려면 해당 유효성 검사 코드를 주석처리 합니다.
- 현재 조건
    - `createReservation` 함수는 Reservation, RentalLog 총 2번 저장을 수행합니다.
    - `rentalLogService.save` 함수는 무조건 `RuntimeException`이 발생합니다.
    - 그럼에도 Reservation 저장은 이루어집니다.
- 개선
    - `createReservation` 함수 내에서
        - 하나라도 에러가 발생하면 모두 저장되지 않도록 수정합니다.
        - 하나라도 에러가 발생하지 않으면 모두 저장되도록 수정합니다.
        - All or Nothing

### **2. 인가에 대한 이해**

![image](https://github.com/user-attachments/assets/954fd38c-9084-4693-bd67-f664a0128152)


- `*AUTH_REQUIRED_PATH_PATTERNS*`: 사용하기 위해 인증이 필요한 API 입니다.
- `*USER_ROLE_REQUIRED_PATH_PATTERNS*`: 사용하기 위해 USER 권한이 필요한 API 입니다.
- `AuthInterceptor`: 로그인 여부를 확인하는 Interceptor 입니다.
- `UserRoleInterceptor`: USER 권한을 확인하는 Interceptor 입니다.
- 현재 조건
    - `/admins` 시작하는 URL을 로그인 한 사용자는 모두가 요청할 수 있다.
- 개선
    - `/admins` 들어오는 요청은 ADMIN 권한을 만들어서 해당 권한이 아니면 요청할 수 없게 만든다.

### **3. N+1에 대한 이해**

![image](https://github.com/user-attachments/assets/ae8675a4-d930-46d8-92af-2e4043714426)


- 설명
    - 모든 예약을 조회하는 기능입니다. 사용자와 물건에 대한 정보를 가져오기 위해 별도로 접근하고 있습니다.
- 현재 조건
    - 모든 예약을 조회할 때 연관된 테이블에 있는 정보를 가져오면서 N+1 문제가 발생합니다.
- 개선
    - 동일한 데이터를 가져올 때 N+1 문제가 발생하지 않게 수정합니다.

### **4. DB 접근 최소화**

![image](https://github.com/user-attachments/assets/288a5de9-eda2-4100-aa8b-b9170693903e)


- 설명
    - 여러 사용자에 대한 신고 기능입니다. 사용자가 가진 상태를 한번에 여러건을 변경할 수 있습니다.
- 현재 조건
    - 위 기능을 수행하기 위해 사용자를 하나씩 찾고 저장하고 있습니다.
    - 데이터가 작을 때는 문제가 되지 않지만 데이터가 많아지면 DB 접근도 함께 증가합니다.
- 개선
    - DB 접근을 최소화하는 방향으로 수정합니다.

### **5. 동적 쿼리에 대한 이해**

![image](https://github.com/user-attachments/assets/3bf6f984-94c1-4cf9-ae6b-6f7e8d7d7dd7)


- 설명
    - `userId`, `itemId` 조건 데이터 존재 여부에 따라 동적으로 검색을 수행합니다.
- 현재 조건
    - 데이터 존재 여부에 따라 다른 JPA가 각각 호출되고 있습니다.
- 개선
    - `QueryDSL`을 활용하여 동적 쿼리를 적용합니다.
    - N+1 문제가 발생하지 않도록 합니다.

### **6. 필요한 부분만 갱신하기**

- 설명
    - `Item` 엔티티 `status` 컬럼  `nullable = false`
- 현재 조건
    - `status`에 대한 값을 전달하지 않을 때 `null` 값이 들어갑니다.
        - `Column 'status' cannot be null` 에러가 발생합니다.
- 개선
    - `DynamicInsert` 활용하여 데이터를 보내지 않은 경우 기본값이 입력되도록 수정합니다.

### **7. 리팩토링**

- 현재 조건
    1. `updateReservationStatus` if-else 과다하게 사용 중
    2. 컨트롤러 응답 데이터 타입 `void`
    3. `findById`가 중복되어 사용 중
    4. 상태 값이 `String`으로 관리 중
- 개선
    1. 필요하지 않은 `else` 구문을 걷어냅니다.
    2. 컨트롤러 응답 데이터 타입을 적절하게 변경합니다.
    3. 재사용 비중이 높은 `findById` 함수들을 `default` 메소드로 선언합니다.
    4. 상태 값을 명확하게 `enum`으로 관리합니다.
    5. 첫번째 Transactional 문제를 해결했다면 `RentalLogService` save 함수 내 19~21번째 코드를 삭제하거나 주석처리하여 기능이 동작하도록 수정합니다.

### **8. 테스트 코드**

- [ ]  **PasswordEncoder 단위 테스트**
    - `@Test` 를 사용해서 PasswordEncoder에 존재하는 메서드들에 대해서 “**단위 테스트”** 를 추가합니다.
- [ ]  **Item Entity Test 추가하기**
    - `@Test` 를 사용해서 Item Entity에 대한 “**단위 테스트”** 를 추가합니다.
    - Item Entity에서 status 값이 `nullable = false` 이므로 해당 제약 조건이 동작하는지 테스트 합니다.
