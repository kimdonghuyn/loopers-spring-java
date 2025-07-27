# 시퀀스 다이어그램

## 1. 브랜드 조회 (GET /api/v1/brands/{brandId})

```mermaid
sequenceDiagram
    participant U as User
    participant BC as BrandController
    participant BF as BrandFacade
    participant BS as BrandService
    U ->> BC: 브랜드 조회 요청 (brandId)
    BC ->> BF: 브랜드 조회 요청 (brandId)
    BF ->> BS: 브랜드 정보 조회 요청 (brandId)
    alt 브랜드가 존재하지 않는 경우
        BF -->> BC: 404 Not Found
    else 브랜드가 존재하는 경우
        BF -->> BC: 브랜드 정보 반환
    end

```

## 2. 상품 목록 조회 (GET /api/v1/products)

```mermaid
sequenceDiagram
    participant U as User
    participant PC as ProductController
    participant PF as ProductFacade
    participant PS as ProductService
    U ->> PC: 상품 목록 조회 요청 (category, brand, sortType)
    PC ->> PF: 상품 목록 조회 요청 (category, brand, sortType)
    PF ->> PS: 상품 목록 조회 요청 (category, brand, sortType)
    alt 상품이 존재하지 않는 경우
        PF -->> PC: 404 Not Found
    else 상품이 존재하는 경우
        PF -->> PC: 상품 목록 반환
    end
```

## 3. 상품 상세 조회 (GET /api/v1/products/{productId})

```mermaid
sequenceDiagram
    participant U as User
    participant PC as ProductController
    participant PF as ProductFacade
    participant PS as ProductService
    U ->> PC: 상품 상세 조회 요청 (productId)
    PC ->> PF: 상품 상세 조회 요청 (productId)
    PF ->> PS: 상품 상세 조회 요청 (productId)
    alt 상품이 존재하지 않는 경우
        PF -->> PC: 404 Not Found
    else 상품이 존재하는 경우
        PF -->> PC: 상품 상세 정보 반환
    end
```

## 4. 상품 좋아요 등록 (POST /api/v1/products/{productId}/likes)

```mermaid
sequenceDiagram
    participant U as User
    participant PC as ProductController
    participant PF as ProductFacade
    participant US as UserService
    participant PS as ProductService
    participant LS as LikeService
    U ->> PC: 상품 좋아요 등록 요청 (X-USER-ID,productId)
    PC ->> PF: 상품 좋아요 등록 요청 (X-USER-ID, productId)
    PF ->> US: 회원 정보 조회 요청 (X-USER-ID)
    alt 회원이 존재하지 않는 경우
        PF -->> PC: 401 Unauthorized
    else 회원이 존재하는 경우
        PF ->> PS: 상품 존재 여부 조회 요청 (productId)
        alt 상품이 존재하지 않는 경우
            PF -->> PC: 404 Not Found
        else 상품이 존재하는 경우
            PF ->> LS: 좋아요 등록 요청 (X-USER-ID, productId)
            alt 좋아요 등록 실패
                PF -->> PC: 500 Internal Server Error
            else 좋아요 등록 성공
                PF -->> PC: 200 OK
            end
        end
    end
```

## 5. 상품 좋아요 취소 (DELETE /api/v1/products/{productId}/likes)

```mermaid
sequenceDiagram
    participant U as User
    participant PC as ProductController
    participant PF as ProductFacade
    participant US as UserService
    participant PS as ProductService
    participant LS as LikeService
    U ->> PC: 상품 좋아요 취소 요청 (X-USER-ID,productId)
    PC ->> PF: 상품 좋아요 취소 요청 (X-USER-ID, productId)
    PF ->> US: 회원 정보 조회 요청 (X-USER-ID)
    US -->> PF: 회원 정보 반환
    alt 회원이 존재하지 않는 경우
        PF -->> PC: 401 Unauthorized
    else 회원이 존재하는 경우
        PF ->> PS: 상품 존재 여부 조회 요청 (productId)
        PS -->> PF: 상품 존재 여부 반환
        alt 상품이 존재하지 않는 경우
            PF -->> PC: 404 Not Found
        else 상품이 존재하는 경우
            PF ->> LS: 좋아요 취소 요청 (X-USER-ID, productId)
            alt 좋아요 취소 실패
                PF -->> PC: 500 Internal Server Error
            else 좋아요 취소 성공
                PF -->> PC: 200 OK
            end
        end
    end
```

## 6. 내가 좋아요 한 상품 목록 조회 (GET /api/v1/users/likes)

```mermaid
sequenceDiagram
    participant U as User
    participant UC as UserController
    participant UF as UserFacade
    participant US as UserService
    participant LS as LikeService
    participant PS as ProductService
    U ->> UC: 좋아요 한 상품 목록 조회 요청 (X-USER-ID)
    UC ->> UF: 좋아요 한 상품 목록 조회 요청 (X-USER-ID)
    UF ->> US: 회원 정보 조회 요청 (X-USER-ID)
    US -->> UF: 회원 정보 반환

    alt 회원이 존재하지 않는 경우
        UF -->> UC: 401 Unauthorized
    else 회원이 존재하는 경우
        UF ->> LS: 좋아요 목록 조회 요청 (X-USER-ID)
        LS -->> UF: 좋아요 목록 반환
        alt 좋아요 내역 없음
            UF -->> UC: 404 Not Found
        else 좋아요 내역 있음
            UF ->> PS: 상품 목록 정보 조회 (productIds)
            alt 상품 정보 없음
                UF -->> UC: 404 Not Found
            else 상품 정보 있음
                UF -->> UC: 좋아요 한 상품 목록 반환
            end
        end
    end
```

## 7. 주문 요청 (POST /api/v1/orders)

```mermaid
sequenceDiagram
    participant U as User
    participant OC as OrderController
    participant OF as OrderFacade
    participant US as UserService
    participant PS as ProductService
    participant OS as OrderService
    participant PG as PaymentGateway
    U ->> OC: 주문 요청 (X-USER-ID, 주문 정보)
    OC ->> OF: 주문 요청 (X-USER-ID, 주문 정보)
    OF ->> US: 회원 정보 조회 요청 (X-USER-ID)

    alt 회원이 존재하지 않는 경우
        OF -->> OC: 401 Unauthorized
    else 회원이 존재하는 경우
        OF ->> PS: 상품 재고 조회 요청 (productId)
        alt 상품 재고가 부족한 경우
            OF -->> OC: 400 Bad Request
        else 상품 재고가 충분한 경우
            OF ->> PG: 결재 요청 (X-USER-ID, 주문 정보)
            alt 결재 요청 실패
                OF -->> OC: HTTP 502 Bad Gateway
            else 주문 요청 성공
                OS -->> OF: 주문 ID, 주문 정보 반환
                OF ->> US: 포인트 차감 요청 (X-USER-ID, 포인트)
                alt 포인트 차감 실패
                    OF -->> OC: 500 Internal Server Error
                else 포인트 차감 성공
                    OF ->> PS: 상품 재고 차감 요청 (productId, 수량)
                    alt 상품 재고 차감 실패
                        OF -->> OC: 500 Internal Server Error
                    else 상품 재고 차감 성공
                        OF ->> OS: 주문 생성 요청 (X-USER-ID, 주문 정보)
                        alt 주문 생성 실패
                            OF -->> OC: 500 Internal Server Error
                        else 주문 생성 성공
                            OF -->> OC: 주문 요청 성공 (주문 ID, 주문 정보)
                        end
                    end
                end
            end
        end
    end
```

## 8. 유저의 주문 목록 조회 (GET api/v1/users/orders)

```mermaid
sequenceDiagram
    participant U as User
    participant UC as UserController
    participant UF as UserFacade
    participant US as UserService
    participant OS as OrderService
    U ->> UC: 주문 목록 조회 요청 (X-USER-ID)
    UC ->> UF: 주문 목록 조회 요청 (X-USER-ID)
    UF ->> US: 회원 정보 조회 요청 (X-USER-ID)

    alt 회원이 존재하지 않는 경우
        UF -->> UC: 401 Unauthorized
    else 회원이 존재하는 경우
        UF ->> OS: 주문 목록 조회 요청 (X-USER-ID)
        alt 주문 목록이 존재하지 않는 경우
            UF -->> UC: 404 Not Found
        else 주문 목록이 존재하는 경우
            UF -->> UC: 주문 목록 반환
        end
    end
```

## 9. 단일 주문 상세 조회 (GET /api/v1/orders/{orderId})

```mermaid
sequenceDiagram
    participant U as User
    participant OC as OrderController
    participant OF as OrderFacade
    participant US as UserService
    participant OS as OrderService
    U ->> OC: 단일 주문 상세 조회 요청 (X-USER-ID, orderId)
    OC ->> OF: 단일 주문 상세 조회 요청 (X-USER-ID, orderId)
    OF ->> US: 회원 정보 조회 요청 (X-USER-ID)

    alt 회원이 존재하지 않는 경우
        OF -->> OC: 401 Unauthorized
    else 회원이 존재하는 경우
        OF ->> OS: 주문 상세 조회 요청 (orderId)
        alt 주문이 존재하지 않는 경우
            OF -->> OC: 404 Not Found
        else 주문이 존재하는 경우
            OF -->> OC: 주문 상세 정보 반환
        end
    end
```