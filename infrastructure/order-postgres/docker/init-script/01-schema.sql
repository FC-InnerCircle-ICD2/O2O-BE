create table order_menu (
    created_at timestamp(6) not null,
    id bigserial not null,
    menu_price bigint,
    menu_quantity bigint,
    total_price bigint,
    updated_at timestamp(6) not null,
    created_by varchar(255) not null,
    menu_id varchar(255),
    menu_name varchar(255),
    order_id varchar(255),
    updated_by varchar(255) not null,
    primary key (id)
);

create table order_menu_option (
    created_at timestamp(6) not null,
    id bigserial not null,
    menu_option_price bigint,
    order_option_group_id bigint,
    updated_at timestamp(6) not null,
    created_by varchar(255) not null,
    menu_option_name varchar(255),
    updated_by varchar(255) not null,
    primary key (id)
);

create table order_menu_option_group (
    created_at timestamp(6) not null,
    id bigserial not null,
    order_menu_id bigint,
    updated_at timestamp(6) not null,
    created_by varchar(255) not null,
    order_menu_option_group_name varchar(255),
    updated_by varchar(255) not null,
    primary key (id)
);

create table orders (
    is_deleted boolean,
    created_at timestamp(6) not null,
    delivery_complete_time timestamp(6),
    delivery_price bigint,
    order_price bigint,
    order_time timestamp(6),
    order_summary varchar(255),
    payment_id bigint,
    payment_price bigint,
    updated_at timestamp(6) not null,
    user_id bigint,
    created_by varchar(255) not null,
    detail_address varchar(255),
    id varchar(255) not null,
    jibun_address varchar(255),
    road_address varchar(255),
    status varchar(255) check (status in ('WAIT','RECEIVE','ACCEPT','REFUSE','COMPLETED','CANCEL')),
    store_id varchar(255),
    tel varchar(255),
    type varchar(255) check (type in ('DELIVERY','PACKING')),
    updated_by varchar(255) not null,
    primary key (id)
);

create table payment (
    type varchar(255) check (type in ('KAKAO_PAY','TOSS_PAY')),
    created_at timestamp(6) not null,
    id bigserial not null,
    payment_price bigint,
    updated_at timestamp(6) not null,
    created_by varchar(255) not null,
    updated_by varchar(255) not null,
    primary key (id)
);
