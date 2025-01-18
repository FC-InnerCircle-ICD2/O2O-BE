INSERT INTO orders (
    id, store_id, user_id, road_address, jibun_address, detail_address,
    tel, status, order_time, order_summary, type, payment_id, is_deleted,
    delivery_complete_time, order_price, delivery_price, payment_price,
    created_at, updated_at, created_by, updated_by
) VALUES (
    'c735a637-621d-4926-9811-909dc2584cf9',  -- ID
    '1006816630',                            -- STORE_ID
    1,                                       -- USER_ID
    '서울특별시 구구구 동동동 123-4',            -- ROAD_ADDRESS
    '서울특별시 구구구 경리단길 123',            -- JIBUN_ADDRESS
    '401호',                                 -- DETAIL_ADDRESS
    '010-1234-5678',                         -- TEL
    'RECEIVE',                               -- STATUS (ENUM)
    '2025-01-16T10:00:00',                   -- ORDER_TIME
    '새우 로제 파스타 외 2개',                  -- ORDER_SUMMARY
    'DELIVERY',                              -- TYPE (ENUM)
    1,                                       -- PAYMENT_ID
    false,                                   -- IS_DELETED
    NULL,                                    -- DELIVERY_COMPLETE_TIME (nullable)
    38900,                                   -- ORDER_PRICE
    500,                                     -- DELIVERY_PRICE
    39400,                                   -- PAYMENT_PRICE
    now(),                                   -- created_at (현재 시간)
    now(),                                   -- updated_at (현재 시간)
    'client',                                -- created_by (작성자)
    'client'                                 -- updated_by (수정자)
);
