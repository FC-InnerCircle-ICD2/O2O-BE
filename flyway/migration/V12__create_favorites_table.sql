create table if not exists public.favorites
(
    id         bigserial,
    user_id    bigint,
    store_id   varchar(255),
    created_by varchar(255),
    updated_by varchar(255),
    created_at timestamp(6) default now(),
    updated_at timestamp(6) default now()
);

create unique index if not exists favorite_idx_1
    on public.favorites (user_id, store_id);
