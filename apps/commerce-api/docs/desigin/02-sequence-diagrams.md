# 시퀀스 다이어그램

## 브랜드
### 브랜드 조회
```mermaid
sequenceDiagram
    actor U as Client
    participant BF as BrandFacade
    participant BS as BrandService
    participant BP as BrandRepository
    participant PS as ProductService
    
    activate U
    U -->> BF: 브랜드 조회 요청
    activate BF
    BF -->> BS: 브랜드 조회  
    activate BS
    BS -->> BP: 브랜드 정보 조회
    activate BP
    alt 브랜드가 존재하지 않음
    BP -->> BS: Optional.empty()

    else 브랜드가 존재함
    BP -->> BS: 브랜드 정보 반환
    BS -->> BF: 브랜드 정보 리턴
    deactivate BP
    deactivate BS
    deactivate BF
    end
    BF -->> PS: 상품 목록 조회
    activate BF
    activate PS
    alt 상품이 존재하지 않는 경우
        PS -->> BF: 빈 배열 리턴
    else 상품이 존재하는 경우
        PS -->> BF: 상품 목록 응답
    end
    
    BF -->> U: 브랜드 정보 반환
    deactivate BF
    deactivate PS
    deactivate U
    
```
## 상품
### 상품 목록 조회
```mermaid

sequenceDiagram
   actor U as Client
   participant PS as ProductService
   participant PP as ProductRepository

   U ->> PS: 상품 목록 조회 요청
   activate PS
   alt 필터링 조건에 만족하지 않을시 (브랜드명,좋아요,생성일,상품명...)
      PS ->> PP: 필터링 조건으로 조회
   activate PP
      PP -->> PS: 빈 리스트 반환
   else 필터링 조건에 만족할시
      PP -->> PS: 상품 목록 리스트 반환
   end
   deactivate PP
   deactivate PS
```
### 상품 상세조회
```mermaid
sequenceDiagram
    actor U as Client
    participant PS as ProductService
    participant PP as ProductRepository
    
    U -->> PS: 상품 상세 조회 요청
    activate PS
    PS ->> PP: 상품 정보 확인         
    activate PP
    alt 상품 ID가 존재하지 않는 경우
    PP ->> PS: Optional.empty()
    else 상품 ID가 존재하는 경우
    PP -->> PS: 상품 정보 반환
    end
    deactivate PP
    deactivate PS
```

## 좋아요
### 좋아요 등록/해제

```mermaid
sequenceDiagram 
actor  C as client
participant LC AS LikeController
participant LF AS LikeFacade
participant LS AS LikeService
participant PS AS ProductService
participant LR AS LikeRepository

C -->> LC: 좋아요 등록/해제 요청
activate C
activate LC
LC -->> LF: 인증요청
activate LF
alt 인증이 실패하는 경우 
LF -->> LC: 401 Unauthorized
deactivate LC
else
%% 상품이 존재하는지 조사
LF -->> PS: 좋아요 등록/해제 할 수 있는 상품 확인
end

activate PS
alt 상품이 존재하지 않는 경우 
PS -->> LF: 404 NotFoundException
else
PS -->> LF: 상품 정보 리턴    
end
deactivate LF
deactivate PS

%% 좋아요 등록
LF -->> LS: 좋아요 등록 여부 확인
activate LF
LS -->> LR: 좋아요 데이터 확인

activate LS
activate LR
alt 이미 등록이 되어 있는 경우
LR -->> LS: 좋아요 데이터 리턴
LS -->> LF: 200 Ok
else
LR -->> LS: 좋아요 데이터 save()   
LS -->> LF: 201 created
end
deactivate LR
deactivate LS
deactivate LF

%% 좋아요 등록 해제
LF -->> LS: 좋아요 해제 여부 확인
activate LF
LS -->> LR: 좋아요 데이터 확인
activate LS
activate LR
alt 좋아요가 등록이 되어있지 않는 경우
LR -->> LS: Optional.empty()
LS -->> LF: 204 no content
else
LR -->> LS: 좋아요 데이터 삭제 delete()
LS -->> LF: 200 OK
end
deactivate LR
deactivate LS
deactivate LF

LF -->> C: 좋아요 등록/해제 반영
deactivate C
```

## 주문/결제
### 주문 생성
```mermaid
sequenceDiagram
actor C as Client
participant OC as OrderController
participant OF as OrderFacade
participant OS as OrderService
participant PAF as PaymentFacade
participant PS as ProductService
participant SS as StockService
participant POS as PointService
participant PG as pgSimulator

C -->> OC: 주문 요청
activate C
activate OC
OC ->> OF: 인증요청
    activate OF
alt 인증이 실패하는 경우
OS ->> OC: 401 Unauthorized
  deactivate OC
else
OF ->> PS: 해당하는 상품이 있는지 확인
  activate PS
end

alt 상품이 존재하지 않는 경우

PS ->> OF: 404 NotFound Exception
else
PS ->> OF: 상품 정보 리턴
  deactivate PS
end

deactivate OF
OS -->> OS: 상태값: 주문중 변경  

OF -->> C: 주문 성공/실패 여부 반환

%% 결제
deactivate C  
C ->> PAF: 결재 요청 (로그인 계정만)

activate C
activate PAF
PAF -->> PG:외부 결제
activate PAF
activate PG
Note over PAF, PG: CompletableFuture / 비동기 호출\nFallback/Timeout/CircuitBreaker 적용

PG -->> PAF: 결제 응답 callback 
deactivate PG
alt 결제 응답이 실패로 리턴되어진 경우
    PAF -->> POS: 포인트 확인
    activate POS
    alt 소지 포인트 보다 상품 가격이 높은 경우 
    POS -->> PAF: 400 Bad Request
    else
    POS -->> PAF: 포인트 차감 (소지 포인트 - 상품가격)  
    end
else 결제 응답이 성공으로 이어진 경우
    PAF -->> OS: 사용 포인트 확인
    activate OS
    alt 사용포인트 + cash보다 상품 가격이 높은 경우
    PAF -->> C: 400 bad Request
    end
    POS -->> PAF: 해당 포인트 차감
end

deactivate POS
deactivate PAF

PAF -->> OF: 결제 완료 상태값 변경 요청
activate PAF


activate OF
deactivate OF

PS -->> PS: 상품 갯수 차감 (상품 재고-)
OS -->> OS: 상태값: 결재 완료 변경 

PAF -->> C: 결제 완료
deactivate PAF
deactivate C
```
### 주문 취소

```mermaid
sequenceDiagram
actor C as Client
participant OC as OrderController
participant OS as OrderService
participant OR as OrderRepository 
C -->> OC: 주문 취소 요청
activate OC
activate C
OC -->> OS: 인증요청
activate OS
alt 인증이 실패하는 경우
    OS -->> OC: 401 Unauthorized
    
else
    OS -->> OR: 주문 데이터 확인
    activate OR
end

alt 주문데이터가 없는 경우
     OR -->> OS: optional.empty()
else
    OR -->> OS: 상태값: 주문 취소 변경
end

OS -->> OC: 200 Ok
deactivate OS

OC -->> C: 주문 취소 결과 반영
deactivate OC
deactivate OR    
deactivate C
```
