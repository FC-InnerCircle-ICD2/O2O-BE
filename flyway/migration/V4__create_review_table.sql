CREATE TABLE reviews
(
    id                       BIGSERIAL PRIMARY KEY,
    order_id                 VARCHAR(255) NOT NULL,
    user_id                  BIGINT       NOT NULL,
    store_id                 VARCHAR(255) NOT NULL,
    client_review_content    TEXT         NOT NULL,
    total_score              INT          NOT NULL CHECK (total_score BETWEEN 0 AND 5),
    taste_score              INT          NOT NULL CHECK (taste_score BETWEEN 0 AND 5),
    quantity_score           INT          NOT NULL CHECK (quantity_score BETWEEN 0 AND 5),
    representative_image_uri VARCHAR(500),
    delivery_quality         VARCHAR(50) CHECK (delivery_quality IN ('GOOD', 'BAD')),
    admin_user_id            BIGINT,
    admin_review_content     TEXT,
    created_by               VARCHAR(255) NULL,
    updated_by               VARCHAR(255) NULL,
    created_at               TIMESTAMP(6) DEFAULT now(),
    updated_at               TIMESTAMP(6) DEFAULT now()
);
