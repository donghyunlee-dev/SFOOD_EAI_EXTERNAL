SFOOD EAI EXTERNAL SERVICE
==

## 📋 프로젝트 개요

> **OMS(Order Management System)** 연동 서비스로, 다양한 외부 쇼핑몰 및 유통사의 **주문 데이터를 수집하고 처리**하는 백엔드 시스템입니다.

External 서비스는 **EAI(Enterprise Application Integration)** 패턴을 적용하여 외부 시스템 간의 데이터 연동을 안정적으로 처리합니다.  
배치 서비스와 OMS에 API 엔드포인트를 제공하여 연동몰에 대한 데이터를 수집하거나 처리할 수 있게 제공합니다.  
외부 연동몰은 OPEN API를 이용하여 데이터를 수집하거나 처리하고, EDI는 DB 배치에 의해 생성된 리테일 주문을 변환하여 OMS 주문 데이터로 수집합니다.

### 🎯 주요 기능

**연동 채널**
- **사방넷** : 오픈마켓 통합 관리 시스템 (네이버, 쿠팡, 11번가 등)
- **고도몰** : 자사 쇼핑몰 (에쓰푸드몰, 존쿡몰, 메디쏠라몰)
- **EDI(리테일)** : 대형 유통사 (이마트, 롯데마트, 농협하나로 등)

**API 서비스 종류**
1. **주문 수집** : 신규 주문 데이터 배치 수집
2. **클레임 처리** : 취소/반품/교환 데이터 배치 수집
3. **상태 동기화** : 주문 상태 양방향 업데이트
4. **배송 연동** : 배송 송장번호 전송
5. **품목 연동** : OMS 품목 정보 등록 및 및 수정 전송
6. **주문 업데이트** : 오류 주문의 마스터 데이터 맵핑 후 상태 업데이트

**실행 방법**
- **스케줄 배치** : 설정된 주기에 따른 자동 실행
- **수동 실행** : OMS 화면을 통한 즉시 실행

---

## 🏗️ 시스템 아키텍처

### 기술 스택

| **Category** | **Technology** | **Version** | **Purpose** |
|--------------|----------------|-------------|-------------|
| **Framework** | Spring Boot | 3.2.9 | 애플리케이션 프레임워크 |
| **Runtime** | Java | 21 LTS | 실행 환경 |
| **Build Tool** | Gradle | 8.x | 빌드 및 의존성 관리 |
| **Database** | Oracle | 19c | 메인 데이터베이스 |
| **ORM** | Spring Data JPA | 3.2.x | 데이터 액세스 계층 |
| **Query DSL** | QueryDSL | 5.0.0 | 타입 안전 쿼리 |
| **HTTP Client** | Apache HttpClient5 | 5.4.1 | 외부 API 통신 |
| **XML Processing** | JAXB | 4.0.2 | XML 데이터 바인딩 |
| **Cloud** | OCI Java SDK | 3.38.0 | Oracle Cloud Storage |
| **Mapping** | MapStruct | 1.4.2 | 객체 매핑 |
| **Monitoring** | P6Spy | 3.9.1 | SQL 로깅 및 성능 모니터링 |
| **Documentation** | SpringDoc OpenAPI | 2.0.2 | API 문서 자동화 |
| **Security** | JSch | 0.1.55 | SSH 터널링 |

## 🌐 연동 시스템 상세

### 🛍️ 고도몰 연동
**연동 개요**
- 고도몰로 운영 중인 3가지 쇼핑몰의 주문 데이터 연동

**연동 쇼핑몰**
- **에쓰푸드몰** : 식품 전문몰 (B2B)
- **존쿡몰** : 델리미트 제품 전문 판매 (B2C)
- **메디쏠라몰** : 건강기능식품 전문몰 (B2C)

**기능 설명**
- **주문 수집**  
  고도몰 주문 상태 중 입금대기(O1), 결제완료(P1) 주문 수집  
  수집된 고도몰의 주문 데이터를 DB에 저장  
  고도몰의 주문 데이터를 OMS 주문으로 변환하여 DB 저장


- **클레임 수집**  
  고도몰 주문 상태 중 자동취소(C1), 품절취소(C2), 관리자취소(C3), 고객취소요청(C4), 반품접수(B1), 교환접수(E1) 주문 수집  
  수집된 고도몰의 주문 데이터를 DB에 저장  
  취소 주문은 원 주문의 주문 상태를 주문취소로 업데이트   
  반품, 교환은 클레임 주문으로 변환하여 DB 저장


- **오류 주문 처리**  
  고도몰 주문의 품목 전체가 OMS 품목 정보 맵핑 성공 시 주문확정 상태  
  고도몰 주문의 품목 중 OMS 품목 정보 맵핑 실패 시 주문 상태를 오류로 변경  
  고도몰 주문의 품목 중 OMS 품목 정보 맵핑 성공이면서 오류인 품목이 있을 경우 주문 상태를 대기로 변경  
  오류 주문인 경우 OMS 품목 등록 후 오류주문 업데이트 기능으로 다시 맵핑하여 정상 주문인 주문 확정으로 변경 가능


- **주문 상태 변경 및 송장 정보 전송**  
  고도몰의 주문 상태 변경 API 하나로 상태 변경과 송장 번호 전송 2가지 기능을 처리  
  OMS에서는 상태 업데이트와 송장 정보를 나눠서 보내고 External에서는 같은 API에 상태코드와 송장번호를 전송


- **품목 정보 전송**  
  OMS의 연동몰 품목 정보에서 자체상품코드 존재 여부로 판단하여 등록 API와 수정 API를 구분해서 고도몰로 전송  
  고도몰로 OMS 품목 정보를 전송해서 등록한 경우 OMS 연동몰 품목 정보에 자체상품코드를 업데이트



**기술 스펙**
- **프로토콜** : REST API (HTTP/HTTPS)
- **데이터 형식** : Json|XML Request/Response
- **인증 방식** : Partner Key, User Key 

**API 엔드포인트**
```
POST /godo/order-collect     # 주문 수집
POST /godo/claim-collect     # 클레임 수집  
POST /godo/order-modify      # 주문 상태 변경
POST /godo/invoice-modify    # 송장 정보 전송
POST /godo/goods-add         # 품목 등록 
POST /godo/goods-modify      # 품목 정보 수정
```

**수집 스케줄** (OMS 설정에 따라 동적 변경)
```yaml
- 에쓰푸드 : 08:00, 11:00 (일 2회)
- 존쿡/메디쏠라 : 매 10분 간격
```

**참고 사이트**
- [고도몰 API 개발자 센터](https://accounts.nhn-commerce.com/login?continue=https://www.nhn-commerce.com/etc/login/gateway.gd?requestURL=https://devcenter.nhn-commerce.com/godomall5/openapi/spec/order_search)
- [존쿡 관리자 사이트](https://gdadmin.johncook.co.kr/base/login.php)
- [에쓰푸드 관리자 사이트](https://gdadmin.sfoodmall.co.kr/base/login.php)
- [메디쏠라 관리자 사이트](http://gdadmin.medisola2.godomall.com/base/login.php)
- [메디쏠라몰 사이트](https://www.medisola.co.kr/goods/goods_list.php?cateCd=036005002)

### 📦 사방넷 연동
**연동 개요**
- 사방넷이 수집한 오픈 마켓의 주문 데이터 연동
- 다중 오픈마켓 통합 관리 플랫폼
- 네이버 스마트스토어, 쿠팡, 11번가, G마켓 등 연동

**기능 설명**
- **주문 수집**  
  사방넷 주문 상태 중 주문확인(002) 주문 수집  
  수집된 사방넷의 주문 데이터를 DB에 저장  
  사방넷의 주문 데이터를 OMS 주문으로 변환하여 DB 저장


- **클레임 수집**  
  사방넷은 2차 쇼핑몰의 주문을 수집하는 구조이기 때문에 클레임 처리가 어렵다고 판단  
  OMS에서 사방넷 주문에 대한 클레임 처리를 하지 않고 직접 1차 쇼핑몰에서 담당자가 처리하기로 함.  
  클레임 수집 기능은 구현되어 있지만 수집 일정을 등록하지 않아 수집 되지 않음.


- **오류 주문 처리**  
  고도몰과 동일하게 품목 맵핑 정보 기준으로 오류 주문을 처리함.


- **배송송장 정보 전송**  
  OMS로부터 받은 배송 송장 번호와 택배사 코드를 사방넷으로 전송  
  택배사 코드는 롯데택배 고정 값으로 전송


- **품목 정보 전송**  
  사방넷의 추가상품등록 & 수정 API를 호출하여 품목 정보를 전송  
  사방넷은 자사품목코드를 전송하지만 자체상품코드를 기준으로 등록 및 수정이 진행됨


**기술 스펙**
- **프로토콜**: REST API (HTTPS Only)
- **데이터 형식**: XML Request/Response
- **인증**: Partner Key

**API 엔드포인트**
```
POST /sabang/order-collect   # 주문 수집
POST /sabang/claim-collect   # 클레임 수집  
POST /sabang/invoice-modify  # 송장 정보 전송
POST /sabang/goods-modify    # 품목 등록 및 수정 
```

**수집 스케쥴** (OMS 설정에 따라 동적 변경)
```yaml
- 에쓰푸드 : 08:20, 08:30, 15:20, 15:30 (일 4회)
- 메디쏠라 : 매 10분 간격
```

**참고 사이트**
- [사방넷 관리자 사이트](https://www.sabangnet.co.kr/index.html)
- [사방넷 API 사이트](https://www.sabangnet.co.kr/RTL_API/guide/)

### 🏢 리테일(EDI) 연동
**연동 개요**
- EDI DB인 MS-SQL의 데이터를 배치에 의해 OMS EDI 데이터 생성
- 스케쥴에 의해 저장된 EDI 주문을 OMS 주문으로 데이터 변환

**주문 연동 유통사**
- **이마트**: 대형마트
- **에브리데이**: 이마트 계열 소형마트
- **롯데슈퍼/롯데마트**: 롯데 계열 유통망
- **농협하나로마트**: 농협 유통 네트워크
- **CS유통**: 편의점 유통망
- **메가마트**: 지역 대형마트

**기능 설명**
- **주문 수집**  
  운영 중인 EDI 서비스의 MY-SQL DB에서 배치에 의해 OMS 내 EDI_ORDERS 테이블에 저장  
  농협 주문과 그 외 리테일로 분리되어 있는 주문을 OMS 마스터 데이터 기준으로 맵핑하여 EDI_ORDER_DATA 테이블에 저장  
  리테일별 스케쥴에 맞춰 EDI_ORDER_DATA의 리테일 주문 데이터를 OMS 주문 데이터로 변환하여 DB 저장
- 
**기술 스펙**
- **연동 방식**: Database Direct Access
- **데이터 처리**: Batch Processing
- **데이터 형식**: 일반 유통 / 농협 주문 데이터 분리

**수집 스케줄**  


| **NAME**     | **BRANCH**     | **ID**                             | **SCHEDULE**                             |
|--------------|----------------|------------------------------------|------------------------------------------|
| **이마트**   | EMART          | RANYE01, RPCHE01, RSHIN01, RSHIN11 | 11:00, 13:20, 14:20, 15:00, 15:30, 16:05 |
| **에브리데이** | EVERYDAY       | RSUPE01                            | 13:00, 15:00, 15:30, 16:10               |
| **롯데슈퍼** | LOTTEMART    | LOSH01                             | 12:00, 12:30, 13:00                      |
| **CS유통**   | CSMART        | CSMART                             | 12:00, 12:30, 13:00                      |
| **롯데마트** | LOTTEMART     | MAGN01                             | 13:30, 13:40, 14:00, 16:30               |
| **메가마트** | MEGAMART      | MEGAMART                           | 12:30                                    |
| **농협하나로** | NORDERS      | 8808983324562                      | 12:30, 14:00                             |


---

## ⚙️ 환경 설정 및 구성

### 🗂️ 프로파일 구성

```yaml
# 환경별 프로파일 설정
local:    # 개발자 로컬 환경
  - 개발 환경 설정
  - SSH 터널링 적용
  - Scheduler 실행 제외
  
dev:      # 개발 서버
  - 개발 서버 환경
  
stage:    # 스테이징 서버  
  - 운영 환경과 동일한 설정
  
prod:     # 운영 서버
  - 운영 환경 설정
```

### 📊 로깅 및 모니터링

**Logback 설정**
```xml
<!-- 환경별 로그 레벨 -->
local: DEBUG, SQL 쿼리 로깅
dev/stage: INFO, 에러 및 성능 로깅  
prod: WARN, 최소한의 로깅
```

**Slack 통합**
- 에러 발생 시 실시간 알림
  
### ☁️ 클라우드 인프라

**Oracle Cloud Infrastructure (OCI)**

**Object Storage 사용**

- 연동몰 API Request Data 용 XML 파일 업로드 용도
```yaml
개발 스토리지 버킷:
  - DEV_XML_Storage: PUBLIC XML 파일 업로드 용
  - PROD_XML_Storage: PRIVATE XML 파일 백업 용
    
운영 스토리지 버킷:
  - OMS_PUB_STORAGE: PUBLIC XML 파일 업로드 용
  - OMS_PRI_STORAGE: PRIVATE XML 파일 백업 용
```

---

## 🏃‍♂️ 빌드 및 배포

### 🔨 로컬 개발 환경

**필수 요구사항**
- Java 21 LTS
- Gradle 8.11.1

**개발 환경 설정**
```bash 
# 의존성 설치 및 빌드
./gradlew clean build

# 테스트 포함 빌드
./gradlew clean build test jacocoTestReport
```


### 🚀 배포 프로세스

**빌드 명령어**
```bash
# 개발 / 프로덕션 빌드
./gradlew clean bootJar
```


**배포 스크립트**
```bash
# 개발 환경 배포
./gradlew build
scp build/libs/*.jar user@bastion:/home/opc/external/
ssh user@bastion:/home/opc/external/shell ". external_deploy.sh"

# 스테이징 환경 배포  
ssh user@bastion:/home/opc/external/shell ". external_stage_deploy.sh"
```
  
### 🔍 헬스체크 및 모니터링

**Actuator 엔드포인트**
```
GET /management/health         # 애플리케이션 상태
GET /management/metrics        # 성능 메트릭
GET /management/info           # 애플리케이션 정보
GET /management/loggers        # 로그 레벨 관리
```

---

## 📁 프로젝트 구조

```
src/main/java/com/sfood/eai/external/
├─annotation            # 어노테이션 정의
├─aop                   # 어노케이션 AOP 구현
├─batch                 # 배치 작업 구현
│  ├─job
│  │  ├─order           # 주문 관련 배치 작업
│  │  │  ├─processor
│  │  │  ├─reader
│  │  │  └─writer
│  │  └─pricing         # 단가 관련 배치 작업
│  │      └─task
│  └─service            # 배치 서비스 구현
│      └─impl
├─component             # 컴포넌트
│  ├─document           # XML 문서 처리 
│  │  ├─godo            # 고도몰 XML 처리
│  │  │  └─data         
│  │  └─sabang          # 사방넷 XML 처리
│  │      ├─data
│  │      └─header      # 사방넷 XML 헤더 분리
│  ├─http               # 연동몰 HTTP 클라이언트  
│  ├─oci                # OCI 연동 구성
├─config                # 환경 설정 구성
├─controller            # REST API 엔드 포인트
├─dto                   # 데이터 전송 객체
│  ├─godo
│  ├─order
│  ├─retail
│  └─sabang
├─entity                # JPA 엔티티 구성    
│  ├─godo
│  ├─goods
│  ├─id                 # 엔티티 키 클래스 
│  │  ├─godo
│  │  ├─goods
│  │  ├─order
│  │  ├─partner
│  │  ├─retail
│  │  ├─sabang
│  │  └─subscription
│  ├─order
│  ├─partner
│  ├─retail
│  ├─sabang
│  └─subscription
├─exception             # 커스텀 예외 구현
├─logger                # 커스텀 로그 구현
├─mapper                # 맵핑 클래스 구성
│  ├─godo
│  ├─order
│  ├─retail
│  └─sabang
├─repository            # 데이터 엑세스 계층
│  ├─godo
│  ├─goods
│  │  └─Impl            # 커스텀 Repository 구현체 클래스
│  ├─order
│  │  └─impl            # 커스텀 Repository 구현체 클래스
│  ├─partner
│  ├─pricing
│  │  └─impl
│  ├─retail
│  │  └─impl            # 커스텀 Repository 구현체 클래스
│  ├─sabang
│  └─subscription
├─scheduler             # Quartz 스케쥴 서비스
│  ├─config
│  ├─job
│  ├─listener
│  └─register
├─service               # 비즈니스 로직 계층
│  ├─converter          # 연동몰 주문 변환 객체    
│  │  └─impl
│  ├─factory            # 팩토리 패턴 처리
│  │  └─impl
│  └─impl
├─type                  # 열거형 상수 
│  ├─godo
│  └─sabang
└─util                  # 유틸 클래스

```

---

## 🧪 테스트 전략

### 🎯 테스트 구조
```
src/test/java/
├─component
│  └─document           # XML 변환 테스트
│      └─godo
├─dto                   # 정기 배송 설정 테스트
│  └─godo
├─entity
│  └─order              # 주문 상태 확인 테스트
├─service
│  └─impl               # 주문 설정 테스트
└─util                  # 문자 / 날짜 변환 테스트
    
```

**테스트 실행**
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests OrderServiceTest
```

---

## 📚 API 문서

**Swagger UI 접근**
```
http://localhost:8093/swagger-ui/index.html
```
---
  

## 📞 지원 및 문의

### 개발 담당자 연락처
- 담당자 : 이동현
- 이메일 : donghyun.lee@s-food.com 
- 연락처 : 010-5335-5548
