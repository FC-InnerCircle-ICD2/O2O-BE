create table public.favorites
(
    id       bigserial not null,
    user_id  bigint,
    store_id varchar(255)
);

create index FAVORITE_IDX_1
    on public.favorites (user_id, store_id);
