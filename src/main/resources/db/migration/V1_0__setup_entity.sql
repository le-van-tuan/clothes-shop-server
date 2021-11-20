CREATE TABLE users(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    name            VARCHAR(255)     NOT NULL,
    email           VARCHAR(255)     NOT NULL      UNIQUE,
    avatar       TEXT,
    enabled         BOOLEAN          DEFAULT TRUE,
    password        VARCHAR(255),
    role            VARCHAR(45)      NOT NULL       DEFAULT 'ROLE_USER',
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users(name, email, avatar, enabled, password, role)
VALUES ('Clothes Admin', 'admin@gmail.com', '', 'TRUE', '$2a$10$Oam.5BWF/2mZUhlQWc8jX.tpoDJaU4YSgIvelDBNqVfTp8yQba6gm', 'ROLE_ADMIN');

CREATE TABLE shipping_addresses(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    name varchar(255) not null,
    user_id       BIGINT     NOT NULL,
    phone_number varchar(50),
    city varchar(255),
    district varchar(255),
    ward varchar(255),
    address varchar(255),
    default_address BOOLEAN DEFAULT false,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE shipping_addresses ADD CONSTRAINT fk_shipping_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE NO ACTION;