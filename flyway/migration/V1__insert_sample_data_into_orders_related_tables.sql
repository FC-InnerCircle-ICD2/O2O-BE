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

INSERT INTO order_menu (
    menu_price, menu_quantity, total_price, created_at, updated_at, created_by, updated_by,
    menu_id, menu_name, order_id
) VALUES
(12400, 1, 12900, now(), now(), 'client', 'client', 'd5010526-60ac-4656-b105-f591a2013435', '[주문폭주] 투움바 파스타 1', 'c735a637-621d-4926-9811-909dc2584cf9'),
(13000, 2, 26000, now(), now(), 'client', 'client', 'd5010526-60ac-4656-b105-f591a2011235', '[주문폭주] 감바스', 'c735a637-621d-4926-9811-909dc2584cf9');

INSERT INTO order_menu_option_group (
    created_at, updated_at, created_by, updated_by,
    order_menu_id, order_menu_option_group_name
) VALUES
(now(), now(), 'client', 'client', 1, '피클 선택'),
(now(), now(), 'client', 'client', 2, '빵 선택');

INSERT INTO order_menu_option (
    created_at, updated_at, created_by, updated_by,
    menu_option_price, order_option_group_id, menu_option_name
) VALUES
(now(), now(), 'client', 'client', 500, 1, '상큼한 피클'),
(now(), now(), 'client', 'client', 500, 2, '마늘빵');

INSERT INTO payment (
    type, created_at, updated_at, created_by, updated_by,
    payment_price
) VALUES
('KAKAO_PAY', now(), now(), 'client', 'client', 39400);
