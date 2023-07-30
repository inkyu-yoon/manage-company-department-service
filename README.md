# 포트폴리오

## 1️⃣ **My Academy**

**학원 관리와 관련된 모든 기능을 제공하는 서비스**

2023.01 ~ 2023.02 **|** Backend(5)

**`Java 11` `Spring Boot 2.7.7` `Spring Security` `JPA` `MySQL 8.0` `AWS EC2` `AWS S3` `Docker` `Nginx` `Redis` `SonarQube`**

Github :  https://github.com/mutsa-team6/myacademy

### 📌 프로젝트 개요

중 · 소형 학원이나 개인 레슨을 운영하는 경영자분들은 학원 관리 프로그램의 비용 부담 때문에 사용하지 못하는 경우가 많습니다.

**My Academy**는, 그런 부분들에 착안하여 학원 규모와 상관없이 학원의 모든 부분(직원, 학생, 강좌, 결제 관리)에 편의성을 제공하는 프로젝트를 제작하게 되었습니다.

학원 별로 학원 등록을 진행하면, 해당 학원을 기준으로 직원들이 회원가입을 할 수 있습니다.

직원들은 학생 · 강사 · 강좌 · 할인 정책 정보를 등록하여 관리할 수 있으며 Toss Pay API를 이용해 강좌 결제 후 결제 내역을 조회할 수 있습니다.

또한, 게시판을 통해 직원 간 공지사항을 전달할 수 있으며 AWS S3를 통해 문서 및 강의자료를 공유할 수 있습니다.

### 국세청 사업자 등록 API 연동을 통한 학원 인증 해결

My Academy를 사용하기 위해서, 가장 먼저 진행해야할 것은 학원 정보 등록 과정입니다.

학원 정보를 등록한 뒤, 해당 학원과 관련된 모든 정보는 해당 학원 소속 회원들만 관리할 수 있는 시스입니다.

**하지만, 실제로 존재하지 않는 학원을 무분별하게 등록하고 사용하게 되면 DBMS 비용과 해당 학원에 대한 조치가 어려울 수 있다는 생각이 들었습니다.** 

따라서, 사업자 등록번호 10자리를 전달하면 학원 존재 및 폐업 진위 여부를 확인할 수 있는 [국세청 API](https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15081808#/%EC%82%AC%EC%97%85%EC%9E%90%EB%93%B1%EB%A1%9D%EC%A0%95%EB%B3%B4%20%EC%A7%84%EC%9C%84%ED%99%95%EC%9D%B8%20API/validate)를 활용했습니다.

비동기 HTTP 통신을 할 수 있으며, HTTP 요청 성공 혹은 실패 처리를 쉽게 할 수 있는 Axios 라이브러리를 활용해 API 요청 및 처리를 진행하였습니다.

[관련 코드](https://github.com/mutsa-team6/myacademy/blob/main/src/main/resources/templates/academy/academies.html#L105)

### OAuth 2.0 프로토콜을 이용한 구글 · 네이버 소셜 로그인 기능 구현

OAuth 2.0이란 구글 · 네이버와 같은 다양한 플랫폼의 특정한 사용자 데이터에 접근하기 위해 제3자 클라이언트(My Academy)가 접근 권한을 위임(Delegated Authorization)받을 수 있는 표준 프로토콜입니다.

My Academy 를 이용하는 사용자에게 네이버 혹은 구글의 Authorization Server 인증을 진행하는 페이지를 연결해주면, 사용자는 해당 페이지에서 로그인을 통해 인증을 수행합니다.

인증이 정상적으로 완료되면, My Academy는 인증을 진행한 사용자의 이메일 · 이름 · 전화번호 등 허가받은 사용자의 정보를 네이버 혹은 구글의 Resource Server로부터 조회할 수 있는 Access Token을 발급받을 수 있습니다.

My Academy는 JWT 기반 로그인 방식을 사용하기 때문에, 발급 받은 Access Token으로 사용자의 이름과 이메일 정보를 획득하고 해당 이름과 이메일로 가입한 회원이 존재하는 경우에 Spring Security 인증 · 인가 과정을 진행하고 사용자 브라우저에 JWT를 쿠키에 저장하게 됩니다.

각 플랫폼의 인증 과정을 거치고 이메일과 이름 정보를 조회하기 때문에 **제 3자가 임의로 정보를 조작해서 인증해 로그인 할 수 없어 보안적으로 안전하고, 사용자는 간소화된 로그인 과정을 통해 편리함을 느낄 수 있습니다.**

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/OauthLogin#1-application-oauth-추가)

### Redis를 이용한 Refresh 토큰 시스템 구현

My Academy는 JWT를 기반으로 사용자의 로그인 상태를 체크하고 권한이 필요한 요청을 처리합니다.

JWT는, 서비스를 사용하는 클라이언트 측에 보관하기 때문에 서버 측에서 세션 상태를 유지할 필요가 없어 서버 확장성과 서버 간 상태를 공유할 필요가 없고 토큰에 저장되어 있는 사용자의 정보를 통해 필요한 요청을 처리할 수 있다는 장점이 있습니다.

반면에, JWT가 탈취되었을 경우 탈취되었는지 확인하기 힘들다는 단점이 존재하기 때문에 토큰의 유효 기간이 길어지면 탈취된 토큰이 오랫동안 사용될 수 있어 짧은 유효기간을 부여하는것이 좋습니다. 

그리고 짧은 유효기간은 사용자에게 토큰이 만료될때마다 지속적으로 인증을 요구하게 된다는 부가적인 단점이 발생하게 됩니다. 따라서 사용하는 방식이 Refresh 토큰 방식입니다.

<img src="https://raw.githubusercontent.com/buinq/imageServer/main/img/image-20230706132225055.png" alt="image-20230706132225055" style="zoom:80%;" />


토큰 특성 상, 유효기간이 지나면 불필요한 데이터가 되어버리고 RDBMS의 부하를 줄이기 위해 토큰 관리는 In-Memory 데이터 스토리지를 사용했습니다. 또한, Memcached 와 비교했을 때 참고자료가 많고 장애 발생 시에도 데이터를 복구할 수 있는 장점이 있기 때문에 Redis를 선택했습니다.

과정은, 사용자에게는 짧은 유효기간을 갖는 Access JWT를 발급하고, Access JWT를 key 값으로 Refresh JWT를 value 값으로 갖도록 Key-Value 데이터 구조인 In-Memory 데이터 스토리지 Redis에 저장합니다.

권한이 필요한 모든 요청은 Access JWT가 유효한지 확인합니다. 만약, Access JWT가 만료된 경우 Redis에 저장되어 있는 Refresh JWT를 조회해서 유효성을 판단합니다. Refresh 토큰이 유효한 경우 Access Token을 새로 발급한 뒤, Redis에도 새로운 Access JWT로 갱신합니다.

Refresh Token 기능을 도입하여, **사용자는 불편한 로그인 과정을 짧은 주기로 하지 않아도 되며 게시글을 작성하거나 정보를 입력하는 도중 토큰이 만료되어 작업에 차질이 생기는 상황을 해결할 수 있게 되었습니다.**

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/RefreshToken)

### SonarQube를 활용하여 코드 리팩토링 및 품질 개선

정적 코드 분석 도구인 SonarQube는 소스 코드를 실행하지 않고 분석합니다. 정적 분석은 소스 코드를 실행하지 않고도 소프트웨어의 구조, 품질, 보안, 성능 등을 분석하는 기법입니다.

코드 품질 및 취약점을 주로 분석하며, 별도의 서버를 통해 코드 push와 함께 workflow를 돌며 분석 결과를 서버에 전송합니다.

크게 Bugs, Code Smell, Security Hotspots의 결함을 찾아내며 각각의 설명은 다음과 같습니다.

> Bugs : 런타임 중 오류로 이어질 수 있는 부분  
> Code Smell : 잠재적으로 코드 유지 보수에 문제가 되는 부분  
> Security Hotspots : 보안상 취약한 부분  

해당 과정으로 코드 추적 결과 Bugs (15 → 0), Code Smell (657 → 226), Security Hotspots (8 → 0)로 개선할 수 있었으며 코드 구조를 변화와 확장에 유연한 구조로 리팩토링할 수 있었습니다.

## 2️⃣ ShoeKream

**한정판 상품 거래 플랫폼 Kream과 유사한 서비스**

2023.03 ~ 2023.05 **|** Backend(2)

**`Java 17` `Spring Boot 3.0.5` `Spring Security` `JPA` `MySQL 8.0` `AWS EC2` `AWS S3` `Docker` `Redis` `NCP(Naver Cloud Platform)`**

Github :  https://github.com/shoe-kream/shoekream

Swagger : http://49.50.162.219:8081/swagger-ui/index.html

### 📌 프로젝트 개요

**ShoeKream**은 사용자가 직접 신발을 판매 · 구매할 수 있고 입찰 혹은 즉시 거래를 선택할 수 있는 상품 거래 플랫폼입니다.

사용자는 원하는 상품을 조회할 수 있으며, 회원 가입 후 이메일 인증을 통해 등급을 올려 상품 거래를 진행할 수 있습니다.

관리자 권한이 있는 경우에만 상품과 브랜드 정보를 관리할 수 있으며 입찰 등록된 상품이 거래가 성사되는 경우 상품 검수를 진행하면서 입찰된 상품 정보를 지속적으로 관리할 수 있습니다.

### HTTP Request Body 데이터 유효성 검증 시 AOP 적용

Spring Validation 라이브러리를 사용하면, 컨트롤러의 @RequestBody 어노테이션이 적용된 DTO에 매핑되는 HTTP 요청의 본문(body) 데이터의 유효성을 검증할 수 있습니다.

@RequestBody 어노테이션이 적용된 매개변수 객체는 클라이언트로부터 전송된 HTTP 요청의 본문 데이터와 자동으로 매핑됩니다. 이때 Spring Validation을 사용하여 해당 DTO에 대한 유효성 검사를 수행할 수 있습니다

비즈니스 로직을 진행하기 전에, 유효한 데이터인지 검증하면 예상하지 못한 에러가 발생하는 것을 줄일 수 있습니다. 

```java
@PostMapping
public ResponseEntity<Response<UserCreateResponse>> create(@Validated @RequestBody UserCreateRequest request, BindingResult br) {

	if (br.hasErrors()) {
    throw new BindingException(br.getFieldError().getDefaultMessage());
	 }	

     UserCreateResponse response = userService.createUser(request);
     return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(response));
}
```

다만, 매핑 과정에서 발생하는 에러를 처리하기 위해 코드가 추가되어 코드 가독성을 헤칠 뿐 아니라 @RequestBody를 사용하는 모든 메서드에 코드가 중복 사용될 것이고 비즈니스 로직과 분리하는 것이 좋다고 판단했습니다.

Spring Aop 라이브러리를 활용하여 Advice(분리할 부가기능)를 정의하고 controller 클래스가 모여있는 패키지를 대상으로 BindingResult 객체가 파라미터에 존재하는 경우 예외처리를 할 수 있도록 구현하였습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/ValidationAop)

### ****Redis를 활용한 사용자 이메일 인증 구현****

상품 거래는 ShoeKream의 가장 핵심적인 서비스이고 신뢰성 있는 거래 서비스를 제공하기 위해 이메일 인증을 완료한 회원만 가능하도록 했습니다.

인증번호는 짧은 유효기간을 갖고 연관관계가 필요한 데이터가 아니기 때문에 In-Memory 데이터 스토리지 Redis를 사용했습니다.

인증 과정은 다음과 같이 구현하였습니다.

1. 사용자의 이메일로 서버측에서 무작위로 생성한 유효시간이 3분인 인증번호(난수)를 보낸다.
2. 서버측은 무작위로 생성한 인증번호와 사용자 이메일을 함께 보관한다.
3. 사용자는 받은 인증번호를 서버에 전달한다.
4. 서버측은 사용자가 보낸 인증번호와 사용자의 이메일을 통해 서버측이 보관하고 있는 인증번호가 일치하는지 확인하고 유효 시간 내에 입력했는지도 확인한다.
5. 인증번호 인증 성공 시, 사용자의 등급을 인증회원으로 변경한다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/RedisAndAuth)

### ****이메일 전송 기능, 비동기 처리를 통한 응답속도 개선****

회원이 비밀번호를 잊어버린 경우, 임시 비밀번호 발급 로직은 다음과 같습니다.

1. 사용자가 비밀번호를 잊어버린 경우, 이메일과 핸드폰 번호 정보를 전달해서 임시 비밀번호 발급을 요청한다.
2. 서버측은 전달한 이메일 · 핸드폰 번호로 가입된 회원이 존재하는지 확인한다.
3. 가입된 회원이 없으면 예외처리하고 존재하면 그 회원의 비밀번호를 임시 비밀번호로 변경한다.
4. 회원에게도 임시 비밀번호를 **이메일로 전송해준다.**

위 과정은 아래와 같이 한 메서드에서 수행되었습니다.

```java
@PutMapping("/find-password")
public ResponseEntity<Response<String>> findPassword(....).. {
		String tempPassword = userService.findPassword(request);
		emailCertificationService.sendEmailForFindPassword(request.getEmail(), tempPassword);
		return ResponseEntity.ok(Response.success("ok"));
}
```

sendEmailForFindPassword 메서드에서는 `JavaMailSender`를 이용해 사용자에게 메일을 전송하게끔 하는데, 메일을 보내기 위한 작업 때문에 사용자에게 성공 응답을 전송하는데  `4.62` 초가 소요되었습니다.

4.62초는 사용자가 불만족감을 느낄정도로 긴 대기 시간이라고 생각이 들었고, 따라서 메일 전송 기능에 비동기 처리를 적용해 응답속도를 개선할 수 있었습니다.

결과적으로 응답속도를 `4.62`초에서 `0.725`초로 약 `6.38`배 개선할 수 있었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/EmailAsync)

### 이메일 전송 기능, AOP 적용을 통한 코드 가독성 · 유연성 향상

ShoeKream은 메일을 임시 비밀번호 발급 · 사용자 인증번호 전송 · 낙찰 성공 정보 전송 등등 여러가지 기능에 사용됩니다. 

이메일 제목과 내용이 항상 같지 않고 차이가 있기 때문에, 단순히 AOP를 적용해서 핵심 로직과 이메일 전송 로직을 분리하기보다는 이벤트 핸들러를 이용해서 발행되는 이벤트에 따라 필요한 메일을 보내도록 구현하였습니다.

`@SendMail` 이라는 커스텀 어노테이션을 정의했고, 메서드로 반환되는 클래스를 AOP 클래스로 전달해 이벤트를 발행하도록 구현할 수 있었습니다.

어노테이션 적용을 통해, 이메일 전송 로직을 비즈니스 로직과 분리할 수 있게되어 코드 가독성 향상과 의존성 제거를 통해 코드 유연성을 향상시킬 수 있었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/EmailAop)

### MySQL Replication 설정 · Write와 Read 작업 분기 처리를 통한 DB 부하 분산 및 고가용성 향상

데이터베이스 부하로 인한 병목 현상을 개선하기 위해 MySQL Replication 기능을 적용하였습니다. 

AWS EC2와 NCP 2개의 서버에 MySQL 도커 컨테이너를 실행시켰습니다. 

Replication 기능을 통해 Master 서버에서 데이터 수정이 발생하면 로그를 기록해서, Slave 서버로 전달하고 변경된 작업이 반영되는 원리입니다.

`@Transactional(readOnly = true)` 이 적용된 메서드가 호출되는 경우, DataSource를 Slave 서버를 사용하도록 하도록 동적 라우팅해서 DB 부하를 분산시키고 고가용성을 향상시킬 수 있게되었습니다.

[관련 블로그 정리 게시글 (MySQL Replication 적용)](https://inkyu-yoon.github.io/docs/Learned/DataBase/mysql-replication)

[관련 블로그 정리 게시글 (동적 라우팅 적용)](https://inkyu-yoon.github.io/docs/Language/SpringBoot/datasource-replication)

## **3️⃣ GrowITh**

**IT 직군 지식 · 정보 공유 커뮤니티 서비스**

2023.03 ~ 2023.05 **|** 개인 프로젝트

**`Java 17` `Spring Boot 3.0.5` `Spring Security` `JPA` `MySQL 8.0` `NCP(Naver Cloud Platform)` `Docker` `Querydsl` `AWS S3`**

Github :  https://github.com/inkyu-yoon/growith

Swagger : http://ec2-3-38-133-211.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html

배포주소 : http://ec2-3-38-133-211.ap-northeast-2.compute.amazonaws.com:8080

### 📌 프로젝트 개요

GrowITh 는 IT 와 관련된 질의응답 · 정보 공유 · 스터디 모집 · 상품 구매를 할 수 있는 서비스 입니다.

회원은 깃허브 계정으로 간편하게 회원가입 및 로그인을 할 수 있으며 게시글 작성 · 수정 · 삭제 · 검색 · 게시글 좋아요 · 댓글 작성 · 수정 · 삭제 · 대댓글 작성 등 커뮤니티에 필요한 기능들을 사용할 수 있습니다.

또한, 마이페이지에서 자신이 작성한 게시글을 확인할 수 있으며 자신이 작성한 게시글에 댓글이나 좋아요가 입력되거나 작성한 댓글에 대댓글이 달린 경우 알림이 발생해 확인할 수 있습니다.

### DockerCompose · Github Actions를 이용한 CI/CD 파이프라인 구축

CI(Continuous Integration)는 코드 변경 사항을 지속적으로 통합하는 과정을 의미하고 CD(Continuous Deployment)는 소프트웨어를 자동으로 빌드하고 테스트한 후에 실시간으로 프로덕션 환경에 배포하는 과정을 의미합니다.

Github Actions를 사용해서 메인 브랜치에 푸시 이벤트가 발생할때 CI/CD가 진행되도록 했습니다.

Gradle 빌드 명령어를 실행하여 소스 코드를 컴파일하고 테스트를 실행합니다. 이를 통해 변경 사항이 지속적으로 통합되고 빌드되는 CI 프로세스가 진행됩니다.

그리고 컨테이너 정보가 담긴 Docker Compose 스크립트와 변경된 사항이 적용되어 빌드된 파일을 도커 컨테이너로 실행시키는 Dockerfile을 실행시키도록 해서 CD 프로세스를 진행하도록 구현하였습니다.

CI 과정을 통해 배포 전 코드 문제점을 신속하게 파악할 수 있었으며 CD 과정을 통해 번거로운 배포 작업을 자동화할 수 있었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Learned/Docker/GitActionsCICD)

### Git Submodule 적용을 통한 각종 설정 정보 파일 관리

Git의 서브모듈은 Git 저장소 안에 다른 Git 저장소를 디렉토리로 분리해 넣을 수 있는 기능입니다.

다른 독립된 Git 저장소를 Clone 해서 내 Git 저장소 안에 포함할 수 있으며 각 저장소의 커밋은 독립적으로 관리할 수 있습니다.

프로젝트 빌드 시, 서브모듈로 관리하는 환경변수가 담긴 스크립트를 사용할 수 있도록하는 구문을 build.gradle에 추가하였습니다.

docker 컨테이너 실행 시, 복잡하게 환경변수를 입력해주지 않아도 되고 스크립트로 환경변수를 관리할 수 있으므로 유지보수성을 향상시킬 수 있습니다.

또한, private repository에 저장하여 권한 없이 접근할 수 없도록 하여 민감한 환경변수의 보안을 향상시킬 수 있었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Learned/Git/GitSubmodule)

### ****QueryDsl를 이용한 쿼리 수와 실행시간 개선****

회원 알림 조회 시, 어떤 게시글에 어떤 회원이 어떤 동작을 해서 알림이 발생되었는지 알려줘야 했습니다.

먼저, 알람 조회를 요청한 **사용자를 DB에서 조회**하고, 그 사용자의 기본키에 해당하는 **알람을 모두 조회**한 후,

그 알람의 갯수만큼 for문을 돌려서 알람 엔티티가 갖고있는 알람을 발생시킨 사용자의 id와 게시글 id를 이용해서 **알람을 발생시킨 사용자를 조회**하고 **알람이 발생한 게시글을 조회**해서 dto를 구성했습니다.

QueryDsl 라이브러리를 사용해서, Alarm, Post, User 테이블을 조인해서 응답 Response Dto를 구성하는 코드를 작성하였고 쿼리 수를 최소 4개에서 1개로 줄였을 뿐만 아니라 java 언어로 SQL 쿼리문을 작성할 수 있었기 때문에 가독성과 유지보수성을 향상시킬 수 있었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/JPA/UseQuerydsl)

### WebClient를 이용한 Github API 활용 · Mock 웹 서버 테스트 코드 작성

Growith는 IT 직군에 관심있는 사용자들이 활동하도록 만든 서비스이기 때문에, 대부분이 GitHub 계정을 갖고 있을 것이라 판단했습니다. 따라서, Github API를 활용해 회원가입과 로그인 단계를 간소화 시켜 접근성과 편의성을 향상시키도록 구현했습니다.

Spring에서 HTTP 요청을 보내고 응답을 받을 수 있도록 지원해주는 RestTemplate와 WebClient 중, Spring이 권장하는 WebClient를 이용해서 GitHub 소셜 로그인을 구현하였습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/GithubLogin)

또한, 외부 API를 사용하는 코드의 테스트 코드를 작성하기 위해 Mock 웹 서버를 구현할 수 있는 WireMock 라이브러리를 사용했습니다. Mock 웹 서버를 사용하는 이유는 다음과 같습니다.

1. 실제 API 서버를 사용해서 테스트를 하면, 서버 상태에 따라 테스트의 결과가 달라질 수 있습니다.
2. Mock 서버로도 충분히 외부 API가 정상이고 정상적인 값(예상한 값)이 반환된다면, 정상적으로 로직이 작동하는 것을 보여줄 수 있습니다.
3. 로컬에 서버를 띄워 사용하기 때문에 속도도 빠르다는 장점이 있습니다.

테스트 코드를 작성하려다 보니, 덕분에 WebClient 객체를 Bean으로 등록한 뒤 DI 받도록 수정할 수 있었고 요청을 보내는 uri도 파라미터로 입력받도록 리팩토링하게 되는 계기가 되었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/WebClientTest)

### ****Request Dto에 있는 Enum 타입 필드 validation 예외처리 개선하기****

Spring Validation 라이브러리를 사용하면, 컨트롤러의 @RequestBody 어노테이션이 적용된 DTO에 매핑되는 HTTP 요청의 본문(body) 데이터의 유효성을 검증할 수 있습니다.

하지만, DTO에 Enum 타입 필드가 존재하는 경우에 문제가 있었습니다.

```java
public enum Category {
    QNA, COMMUNITY, STUDY, NOTICE;
    }
}
```

위와 같은 Enum 타입 필드가 있을 때, validation 라이브러리의 `@Notnull` 어노테이션으로 null 값이 입력되는 상황을 처리할 수 있었지만 Enum으로 정의되지 않은 문자열이 입력됐을 때는 처리가 되지 않았습니다. 또한, `qna` 와 같이 소문자로 입력되는 경우에도 처리되지 않았습니다.

Json데이터를 역직렬화 하는 과정을 수동 설정하는 코드를 작성해서, Enum 타입 필드 매핑 예외 처리를 할 수 있었고 에러 응답을 통해 어떤 에러가 발생했는지 사용자에게 명시할 수 있게 되었습니다.

[관련 블로그 정리 게시글](https://inkyu-yoon.github.io/docs/Language/SpringBoot/EnumValidation)

### AWS S3 버킷 연동을 통한 이미지 파일 관리

게시글 작성 시, 사진이나 파일을 첨부하는 기능이 필요하다고 생각했습니다.

RDBMS가 있는 EC2 서버에 그대로 올리게 되면 DB 부하가 심해지고 데이터 용량에 따라 비용도 많이 발생한다는 것을 알게되었고, 데이터를 오브젝트 형태로 저장하는 서비스인 AWS S3를 사용하게 되었습니다.

AWS S3에 파일을 업로드 하면, 해당 파일을 확인할 수 있는 `url` 이 생성되고, 이를 사용자에게 보여줌으로서 접근 가능하도록 구현하였습니다.

이를 통해, 내구성과 확장성을 보장하고, 이미지,텍스트를 포함한 파일들을 효율적으로 관리할 수 있었습니다.

[관련 블로그 정리 게시글 (S3 환경 세팅)](https://inkyu-yoon.github.io/docs/Learned/TIP/s3-setting)

[관련 블로그 정리 게시글 (스프링 부트 설정)](https://inkyu-yoon.github.io/docs/Language/SpringBoot/s3-image-upload)
