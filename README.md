## 🍃 스프링 부트 쇼핑몰 프로젝트

스프링 부트를 활용한 간단한 쇼핑몰 프로젝트입니다.
<br>

### 🏃 프로젝트 기간
24.04.29 - 24.05.12

### 👤 프로젝트 유형
개인 프로젝트

### 📋 개발 Tool

- IntelliJ
- DBeaver
  
## 📋 사용 기술

### BE
- Spring boot
- JAVA 17
- Spring Boot Security
- JPA

### FE
- Javascript
- Thymeleaf

### Build tool
- Gradle

### Database
- Mysql

### DB Hosting
- Azure 

### AWS
- S3
- Elastic Beanstalk

## 📌 주요 기능 

### 로그인/회원가입

- 유저는 회원가입 및 로그인을 사용할 수 있습니다.
- 시큐리티를 활용해 유저의 정보를 db에 저장합니다.
- 패스워드는 해싱하여 저장합니다.
- 회원가입시 중복 아이디를 구분합니다.

  
![image](https://github.com/hhhyeon97/shop2/assets/148893126/75f7d3d9-2e96-4347-aa40-b9f77ab4e00f)|![solsite4](https://github.com/hhhyeon97/shop2/assets/148893126/afd71f82-587a-4b3e-84e4-71dbf8eaae37)
-|-|

### 회원 정보 수정

- 로그인한 유저는 마이페이지를 사용할 수 있습니다.
- 마이페이지에서는 해당 유저의 주문, 리뷰 내역을 확인할 수 있습니다.
- 유저는 닉네임과 비밀번호를 재설정할 수 있습니다.

![solsite1](https://github.com/hhhyeon97/shop2/assets/148893126/227d5292-d012-4edd-8d6e-9648db8e6f71)

### 상품 정렬

- 유저는 등록된 상품 목록을 확인할 수 있습니다.
- 페이지네이션을 적용하여 각 페이지로 이동할 수 있습니다.
- 유저는 최신순, 가격순, 이름순으로 정렬된 상품을 확인할 수 있습니다.<br>
  기본적으로는 상품 id를 기준으로 내림차순 정렬로 설정하였습니다.

![solsite2](https://github.com/hhhyeon97/shop2/assets/148893126/dd47f804-689b-4c18-99e9-4bde910a64e5)

### 상품 등록

- 관리자 권한을 가진 계정으로 상품을 등록할 수 있습니다.
- S3를 활용해 이미지를 저장하고 DB에는 S3에 저장된 이미지의 URL을 저장합니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/59a13f4a-8131-4255-bc3d-d7ea220be8ba)

### 상품 수정/삭제

- 관리자는 상품을 수정/삭제 할 수 있습니다.
- 상품을 삭제하면 S3에 저장된 이미지도 함께 삭제됩니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/373e32fb-095d-4ca9-b66a-b6050b07dc80)|![solsite3](https://github.com/hhhyeon97/shop2/assets/148893126/9707f726-2a6a-4306-b588-b3d9753658e8)
-|-|


### 상품 주문/리뷰 작성

- 상품 상세 페이지에서는 상품 개수를 입력하여 해당 금액으로 주문이 가능합니다.
- 상품을 주문한 유저만 해당 상품의 리뷰를 작성할 수 있습니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/6cdd5827-970f-4598-9f07-2c4c93686eae)


### 상품 검색

- 상품을 검색하면 검색 결과가 나타납니다.
- 검색 결과의 이미지를 클릭하면 해당 상품의 상세 페이지로 이동합니다.
  
![image](https://github.com/hhhyeon97/shop2/assets/148893126/db0e4d1d-ba76-4231-be35-8b2e9c3bf255)

### 관리자 페이지

- 관리자 계정으로 로그인시 메뉴바에 관리자 페이지 메뉴가 나타납니다.
- 관리자 관련 페이지는 관리자 권한을 가진 계정으로만 접근할 수 있습니다.
- 관리자 페이지에서는 회원 목록, 전체 리뷰 내역, 전체 주문 내역을 확인할 수 있습니다.
- 회원 목록 페이지에서는 회원 탈퇴를 수행할 수 있습니다.
  
![solsite5](https://github.com/hhhyeon97/shop2/assets/148893126/87c38472-44d1-4d9e-86f7-cdad69dc4cac)


