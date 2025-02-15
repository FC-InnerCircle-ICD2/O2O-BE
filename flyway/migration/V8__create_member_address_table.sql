CREATE TABLE member_address
(
    id                       BIGSERIAL PRIMARY KEY,
    user_id                  BIGINT NOT NULL,
    address_type             VARCHAR(255) NOT NULL,
    road_address             VARCHAR(255) NOT NULL,
    jibun_address            VARCHAR(255) NOT NULL,
    detail_address           VARCHAR(255),
    latitude                 DOUBLE PRECISION NOT NULL,
    longitude                DOUBLE PRECISION NOT NULL,
    alias                    VARCHAR(255),
    is_default               BOOLEAN NOT NULL,
    is_deleted               BOOLEAN NOT NULL,
    created_by               VARCHAR(255) NULL,
    updated_by               VARCHAR(255) NULL,
    created_at               TIMESTAMP(6) DEFAULT now(),
    updated_at               TIMESTAMP(6) DEFAULT now()
);
