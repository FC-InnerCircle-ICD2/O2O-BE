UPDATE  public.orders
SET     id = TO_CHAR(created_at, 'YYYYMMDD') || '-' || UPPER(SUBSTRING(id, 1, 8))
WHERE   id NOT LIKE '2025%';

UPDATE  public.order_menu
SET     order_id = TO_CHAR(created_at, 'YYYYMMDD') || '-' || UPPER(SUBSTRING(order_id, 1, 8))
WHERE   order_id NOT LIKE '2025%';
