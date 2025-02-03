insert into members (role, state,
                     sign_name, password,
                     user_name, nick_name,phone,
                     created_at, updated_at, created_by, updated_by)
values ('CEO', 'JOIN',
        'admin1@gmail.com', '$2a$10$lQMVZxWFenIOT.R3Ev1u..e3nXPL0YVOLUCsarMLloWp5FX4SSzDC',
        '홍길동', '개발의민족-점주1', '01012345678',
        now(), now(), 'init', 'init');

insert into members (role, state,
                     sign_name, password,
                     user_name, nick_name,phone,
                     created_at, updated_at, created_by, updated_by)
values ('CEO', 'JOIN',
        'admin2@gmail.com', '$2a$10$MRP0DMMzJ8uY3eyqGd7K/uhARcvkmMgO78neiC3LyG84Q4RWRa/wG',
        '홍길순', '개발의민족-점주2', '01012345678',
        now(), now(), 'init', 'init');

insert into members (role, state,
                     sign_name, password,
                     user_name, nick_name,phone,
                     created_at, updated_at, created_by, updated_by)
values ('USER', 'JOIN',
        'user1@gmail.com', '$2a$10$UCYHYTGYf0cfAcCb/Q77qekWt.p/2VJIJcZnGG.zrCp7W8bVwUZjK',
        '홍길똥', '개발의민족-유저1', '01012345678',
        now(), now(), 'init', 'init');

insert into members (role, state,
                     sign_name, password,
                     user_name, nick_name,phone,
                     created_at, updated_at, created_by, updated_by)
values ('USER', 'JOIN',
        'user2@gmail.com', '$2a$10$5QFPTZgyZJIG/KTcBiNaeuAjwOVdeWLVorZKwUK68kShilU5.EN4O',
        '홍길쑨', '개발의민족-유저2', '01012345678',
        now(), now(), 'init', 'init');

UPDATE public.orders
SET user_id = 3 -- 사용자 user1@gmail.com 으로 매핑
WHERE id = 'c735a637-621d-4926-9811-909dc2584cf9'
