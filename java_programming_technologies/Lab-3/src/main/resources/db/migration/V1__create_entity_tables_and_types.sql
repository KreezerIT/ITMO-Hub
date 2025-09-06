DO
$$
    BEGIN
        CREATE TYPE pet_color AS ENUM ('BLACK', 'WHITE', 'BROWN', 'GRAY', 'GOLDEN');
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

CREATE TABLE IF NOT EXISTS owner
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    birth_date DATE
);

CREATE TABLE IF NOT EXISTS pet
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    birth_date  DATE,
    breed       VARCHAR(255),
    color       VARCHAR(255),
    tail_length INT DEFAULT 0,
    owner_id    BIGINT,
    CONSTRAINT pet_owner_id_fkey FOREIGN KEY (owner_id)
        REFERENCES owner (id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS pet_friends
(
    id        BIGSERIAL PRIMARY KEY,
    pet_id    BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    CONSTRAINT fk_pet FOREIGN KEY (pet_id) REFERENCES pet (id) ON DELETE CASCADE,
    CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES pet (id) ON DELETE CASCADE,
    CONSTRAINT chk_pet_less_than_friend CHECK (pet_id < friend_id),
    CONSTRAINT unique_friendship UNIQUE (pet_id, friend_id)
);