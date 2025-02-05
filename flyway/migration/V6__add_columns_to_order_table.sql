alter table public.orders
    add excluding_spoon_and_fork bool;

alter table public.orders
    add request_to_rider varchar(255);
