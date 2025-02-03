alter table public.payment
    add status varchar(255);

UPDATE public.payment SET status = 'COMPLETED' WHERE id = 1
