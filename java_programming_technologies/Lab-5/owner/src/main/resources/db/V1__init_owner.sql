CREATE TABLE IF NOT EXISTS owner
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    birth_date DATE,
    CONSTRAINT owner_pkey PRIMARY KEY (id)
);