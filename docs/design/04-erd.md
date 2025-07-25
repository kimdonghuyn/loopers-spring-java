# 전체 테이블 구조 및 관계 정리

```mermaid
erDiagram
    MEMBER {
        id Long PK
        login_id varchar
        name varchar
        email varchar
        gender varchar
        birth DateTime
        created_at DateTime
        updated_at DateTime
        deleted_at DateTime
    }
    
    POINT{
        id Long PK
        ref_user_id Long FK
        point Integer
        created_at DateTime
        updated_at DateTime
    }

    PRODUCT {
        id Long PK
        ref_category_id Long
        ref_brand_id Long
        name varchar
        price Long
        stock Integer
        created_at DateTime
        updated_at DateTime
        deleted_at DateTime
    }

%%    CATEGORY {
%%        id Long PK
%%        type varchar
%%        name varchar
%%        depth Integer
%%    }

    BRAND {
        id Long PK
        name varchar
    }

    LIKE {
        id Long PK
        ref_product_id Long FK
        ref_user_id Long
    }

    ORDER {
        id Long PK
        ref_user_id Long
        ref_product_id Long
        quantity Integer
        status varchar
        create_at DateTime
        updated_at DateTime
        deleted_at DateTime
    }
    
    ORDER_ITEM {
        id Long PK
        ref_order_id Long FK
        ref_product_id Long FK
        quantity Integer
        price Long
    }

%%    PRODUCT ||--o{ CATEGORY: belongs_to
    PRODUCT ||--o{ BRAND: belongs_to
    PRODUCT ||--o{ LIKE: has_many
    MEMBER ||--o{ LIKE: has_many
    MEMBER ||--o{ ORDER: has_many
    MEMBER ||--o{ ORDER_ITEM: has_many
    ORDER ||--o{ ORDER_ITEM: has_many
    ORDER_ITEM ||--|| PRODUCT: has_one
    MEMBER ||--|| POINT : has_one
```