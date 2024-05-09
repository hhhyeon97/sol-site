## 🍃 스프링 부트 쇼핑몰 프로젝트

스프링 부트를 활용한 간단한 쇼핑몰 프로젝트입니다.
<br>

### 🏃 프로젝트 기간
24.04.29 - 24.05.09

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

  
![image](https://github.com/hhhyeon97/shop2/assets/148893126/75f7d3d9-2e96-4347-aa40-b9f77ab4e00f)|![image](https://github.com/hhhyeon97/shop2/assets/148893126/4aa67082-ae58-4024-a1e7-16480cc41683)
-|-|

### 마이페이지

- 로그인한 유저만 마이페이지에 접속할 수 있습니다.
- 마이페이지에선 자신의 정보와 주문, 리뷰 내역을 확인할 수 있습니다.

![solsite1](https://github.com/hhhyeon97/shop2/assets/148893126/227d5292-d012-4edd-8d6e-9648db8e6f71)

### 상품 목록

- 유저는 등록된 상품 목록을 확인할 수 있습니다.
- 각 페이지로 이동할 수 있습니다.
- 로그인한 유저에게만 수정, 삭제 버튼이 보입니다.

![solsite2](https://github.com/hhhyeon97/shop2/assets/148893126/dd47f804-689b-4c18-99e9-4bde910a64e5)

### 상품 등록

- 로그인한 유저는 상품을 등록할 수 있습니다.
- S3를 활용해 이미지를 저장하고 DB에는 S3에 저장된 이미지의 URL을 저장합니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/59a13f4a-8131-4255-bc3d-d7ea220be8ba)

### 상품 수정/삭제

- 상품의 수정과 삭제는 해당 상품을 등록했던 유저만 가능합니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/cb51943b-bfd7-4b10-a53a-105afa49beac)|![solsite3](https://github.com/hhhyeon97/shop2/assets/148893126/9707f726-2a6a-4306-b588-b3d9753658e8)
-|-|


### 상품 구매/리뷰 작성

- 상품 상세 페이지에선 상품 개수를 입력하여 해당 금액으로 주문이 가능합니다.
- 주문한 내역은 마이페이지에서 확인 가능합니다.
- 상품을 주문한 유저만 해당 상품의 리뷰를 작성할 수 있습니다.

![image](https://github.com/hhhyeon97/shop2/assets/148893126/d27effe1-c0c2-43c7-8c13-c5de856a6d00)


### 상품 검색

- 상품을 검색하면 상품의 제목을 비교한 검색 결과가 나타납니다.
- 검색 결과의 이미지를 클릭하면 해당 상품의 상세 페이지로 이동합니다.
  
![image](https://github.com/hhhyeon97/shop2/assets/148893126/db0e4d1d-ba76-4231-be35-8b2e9c3bf255)


## 💭 느낀 점 
