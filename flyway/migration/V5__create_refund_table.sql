CREATE TABLE refunds (
     id          BIGSERIAL PRIMARY KEY,
     order_id    VARCHAR(255) NOT NULL,
     status      VARCHAR(10) NOT NULL CHECK (status IN ('WAIT', 'COMPLETE', 'FAIL')),
     created_by  VARCHAR(255) NULL,
     updated_by  VARCHAR(255) NULL,
     created_at  TIMESTAMP(6) DEFAULT now() NULL,
     updated_at  TIMESTAMP(6) DEFAULT now() NULL
);
