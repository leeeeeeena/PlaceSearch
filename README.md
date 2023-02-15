# 장소검색 서비스

![image](https://user-images.githubusercontent.com/125448783/219024559-e64398a2-a70d-4eac-b041-b285834cd6e6.png)
   

### 요구사항

**1. 장소검색**

* 카카오 검색 API, 네이버 검색 API를 통해 각각 최대 5개씩 총 10개의 키워드관련 장소 검색
* 장소 검색 결과를 기준으로 동일하게 나타나는 장소가 상위에 오도록 정렬

</br>

**2. 검색 키워드 목록**

* 사용자들이 많이 검색한 순서대로 최대 10개의 검색 키워드 목록 제공
* 키워드 별로 검색된 횟수도 함께 표기

</br>

[문제해결 전략은 여기로](#핵심-문제-해결-전략)

# 테스트 방법

### 저장소 설정

`application.yml` 파일의 redis 설정 환경에 맞게 수정

```
  redis:
    port: {redis-port}
    host: {redis-host}
    password: {redis-password}
```
</br>

Redis가 설치되어 있지 않은 경우 deploy 패키지의 `redis.yml` 을 다운로드 받은 후 해당 명령어 실행

```
docker-compose -f redis.yml up -d
```

</br>

### API 요청 cURL

1. 장소검색

`은행` 키워드를 UTF-8 로 인코딩하여 요청 예시

```
curl -X GET --location "http://localhost:9090/v1/search/place?keyword=%EC%9D%80%ED%96%89" \
    -H "Accept: application/json" \
    -H "Content-Type: application/json;charset=UTF-8"
```
</br>

2. 검색 키워드 목록

```
curl -X GET --location "http://localhost:9090/v1/search/rank/keyword" \
    -H "Accept: application/json" \
    -H "Content-Type: application/json;charset=UTF-8"
```



<br>

## 사용 기술

| 분야                   | stack                             |
| ---------------------- | --------------------------------- |
| 언어                   | Java (zulu 11)             |
| 프레임워크             | springBoot 2.4.2                  |
| DB                     | Redis |
| 빌드 툴                | Maven                            |
| API 테스트 툴          | Junit                           |
| IDE                    | IntelliJ                          |

<br>

## 라이브러리

| 라이브러리                                                                                                                          | 이유                                      |
| ----------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------- |
| [WATSAPP](https://github.com/eoqkrskfk94/WATSProject/tree/master/app/src/main/java/com/handong/wats/wheeliric/Convert) KATEC To Geo | 네이버 API 좌표 latitude, longtitude 변환 |
| org.apache.httpcomponents   |  외부API 연동 호출시 HttpClient 사용     |
| Lombok       |   어노테이션 사용                                          |
| Spring Web   |   HTTP 요청 API 구현 및 서블릿 환경 사용       |
| Spring Cloud   |   circuit breaker 구현을 위해 사용       |
| Spring Redis   |   spring framework에서 redis 코드레벨 구현을 위해 사용       |

<br>


# API 명세

## 키워드 검색 API

#### 요청 규격

| 이름    | 타입   | 필수 | 설명             |
| ------- | ------ | ---- | ---------------- |
| keyword    | string | ○    | 검색 키워드      |

</br>

#### 응답 규격

| 이름    | 타입   | 설명             |
| ------- | ------  | ---------------- |
| total    | int  | 키워드 검색 결과 개수       |
| places | Place 객체  | 장소 결과 리스트 |


##### Place

| 이름    | 타입 | 설명             |
| ------- | ------| ---------------- |
| placeName    | string | 키워드 검색 결과 개수   |
| address | string | 장소 결과 리스트 |
| roadAddress    | string | 키워드 검색 결과 개수   |
| x | string | 장소 결과 리스트 |
| y    | string | 키워드 검색 결과 개수       |
| phoneNumber | string | 장소 결과 리스트 |



</br>

#### 응답 예시

```json

{
    "places": [
        {
            "placeName": "하나은행 본점",
            "address": "서울 중구 을지로1가 101-1",
            "roadAddress": "서울 중구 을지로 35",
            "x": "126.981866951611",
            "y": "37.566491371702",
            "phoneNumber": "1599-1111"
        },
        {
            "placeName": "KB국민은행 여의도본점",
            "address": "서울 영등포구 여의도동 36-3",
            "roadAddress": "서울 영등포구 국제금융로8길 26",
            "x": "126.927905661537",
            "y": "37.5208657732053",
            "phoneNumber": "02-2073-7114"
        },
        {
            "placeName": "KB국민은행 영등포지점",
            "address": "서울 영등포구 영등포동4가 68-2",
            "roadAddress": "서울 영등포구 영등포로 208",
            "x": "126.904255816231",
            "y": "37.51968487408",
            "phoneNumber": "02-2675-0551"
        }
    ],
    "total": 3
}


```

<br>

## 검색 키워드 목록 API


#### 응답 규격

| 이름    | 타입   | 설명             |
| ------- | ------  | ---------------- |
| total    | int  | 키워드 top 10 검색 결과 개수      |
| rankedKeywords | RankedKeyword 리스트  | 상위 랭크된 키워드 |


##### RankedKeyword

| 이름    | 타입 | 설명             |
| ------- | ------| ---------------- |
| keyword    | string | 키워드   |
| searchCount | int | 검색된 횟수 |

<br>

#### 응답 예시

```json
{
    "rankedKeywords": [
        {
            "keyword": "분당 맛집",
            "searchCount": 50
        },
        {
            "keyword": "서점",
            "searchCount": 26
        },
        {
            "keyword": "판교 맛집",
            "searchCount": 24
        },
    ],
    "total": 3
}


```

<br>

## 요청 공통


#### `헤더`

| 항목         | 값 (예)          | 설명            |
| ------------ | ---------------- | --------------- |
| Content-Type | application/json;charset=UTF-8| `JSON`으로 요청. `UTF-8` 인코딩 | 
| Accept | application/json | `JSON` 허용 | 

<br>

## 응답 공통


#### `HTTP 응답코드`

| 응답코드 | 설명                  |
| -------- | --------------------- |
| `200`    | **정상 응답**         |
| `201`    | **정상적으로 생성**   |
| `400`    | 잘못된 요청           |
| `404`    | 리소스를 찾을 수 없음 |
| `500`    | 시스템 에러           |

</br>

#### `에러코드 및 메시지`

| 에러코드           | 메시지                             |
| ------------------ | ---------------------------------- |
| 외부 API 연동 오류 |                                    |
| `0000`             | 알 수 없는 오류가 발생하였습니다   |
| `0001`             | 허가 되지 않은 요청입니다          |
| `0002`             | 외부 요청 생성에 실패하였습니다    |
| `0003`             | 외부 요청 대상이 존재하지 않습니다 |
| `0004`             | 지원이 되지 않는 외부 요청입니다   |
| `0005`             | 요청 값이 올바르지 않습니다        |
| `0100`             | 연동 API 연결 실패하였습니다       |
| `0101`             | 연동 API 연결이 오래 걸립니다      |
| 사용자 요청 오류   |                                    |
| `4000`             | 잘못 된 요청입니다                 |
| `4100`             | 인증 되지 않은 요청입니다          |
| `4200`             | 지원하지 않는 요청입니다           |
| `4201`             | 지원하지 않는 요청입니다           |
| `4202`             | 잘못 된 요청입니다                 |
| `4300`             | 필수 값이 누락되었습니다           |
| `4301`             | 요청 값이 유효하지 않습니다        |
| `4302`             | 요청 값이 허용되지 않습니다        |
| `4400`             | 요청 대상을 찾을 수 없습니다       |
| `4401`             | 요청 대상이 중복 되었습니다        |
| `4402`             | 요청을 처리할 수 없습니다          |
| 서버 내부 오류     |                                    |
| `5000`             | 알 수 없는 오류가 발생하였습니다   |
| `5001`             | 허가 되지 않은 요청입니다          |
| `5100`             | DB 연결 실패하였습니다             |
| `5101`             | DB 연결이 오래 걸립니다            |
| `5200`             | MW 연결 실패하였습니다             |
| `5201`             | MW 연결이 오래 걸립니다            |

</br>

#### 에러응답

| 이름    | 타입    | 설명             |
| ------- | ------ |  ---------------- |
| code    | string | 응답 코드        |
| message | string |  API 별 응답 내용 |

</br>

#### 에러응답 예시

```json
{
  “code”: “4000",
  “message”: “잘못 된 요청입니다”
}
```

</br>

## 핵심 문제 해결 전략

### 1. 의존성 줄이기

![image](https://user-images.githubusercontent.com/125448783/219016583-239f1ea3-3e97-4d7a-be8c-5cb9263f097d.png)


요구사항 중에 `구글 장소 검색 등 새로운 검색 API 제공자의 추가 시 변경 영역 최소화에 대한 고려` 가 있었다.   
의존도를 줄이기 위해 `장소 검색 인터페이스(SearchService)`를 만들고, `라우터(ExternalSearchServiceRouter)`로 해당 인터페이스의 구현체인 KakaoSearchService와 NaverSearchService를 호출하게 하였다. 이를 통해 검색 API제공자가 추가되어도 SearchService의 코드 변경 없이 동작할 수 있도록 구현하였다.      
검색결과를 정렬하는 알고리즘도 바꿔끼울 수 있게 `ResultSort 인터페이스`로 의존관계를 주입해주었고, 마찬가지로 키워드 검색 횟수를 카운트 하기 위한 `레포지토리(SearchRepository)`도 Redis, JPA 등 다양하게 바꿔 사용할 수 있도록 인터페이스로 정의해주었다. 

</br>

### 2. 키워드 별로 검색된 횟수

키워드에 대한 데이터를 저장하려면, DB가 필요하다. 
대용량 트래픽 처리를 위한 반응성(Low Latency), 확장성(Scalability), 가용성(Availability) 을 고려하여 어떤 DB가 가장 좋을지 고민하였다.    
결론적으로 Redis를 선택했다.   

option-1. Redis(선택)

Redis는 cache 처리가 되어 빠르다. 때문에 반응성(Low Latency)이 높다. single thread 기반으로 동작하지만 동시성이 존재한다.(병렬x 동시o)   
@Transactional을 사용하기 위해서는 Redisson을 사용해야 하는데 Spring에서 공식적으로 사용하는 라이브러리가 아니라서 에러 발생시 자료부족으로 대응하기 어려울 수 있겠다는 생각이 들어 제외하고 lettuce를 선택하였다. 
[RedisCacheManager](https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/cache/RedisCacheManager.html) 를 사용하여 구현하되, redistemplate에서 자체 제공하는 함수를 사용하면 함수 내에서는 원자성이 보장되므로 `조회, 업데이트를 분리하지 않고 사용`해주었다.   
고도화된 아키텍처에서는 Redis Queue를 사용해서 확장성과 가용성을 높여 설계할 수도 있겠다.


</br>

option-2. RDBMS (JPA)

데이터를 영구적으로 보관할 수 있고 레퍼런스도 많다. @Transactional을 통해 동시성 처리도 할 수 있다.    
데이터 정합성을 필요로 하는 서비스의 경우 NoSQL보다 RDBMS를 사용하는 것이 맞으나, 이 경우 구조가 간단하고 데이터 정합성이 크게 중요하지 않았다. 무엇보다 Redis가 더 빠르다. 

</br>

optioin-3. Kafka Connect

서버 하나에 오류가 나도 middleware로 관리하면 장애가 전이되지 않고 구현할 수 있고 데이터베이스간 동기화도 용이하다.    
Kafka Connect + NoSQLDB 로 구현하면 서버에 장애가 나도 kafka에 들어간 데이터는 무사히 업데이트 할 수 있다.   
추후에 고도화로 진행해보고 싶다.     

</br>


