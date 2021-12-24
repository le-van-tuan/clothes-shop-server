CREATE TABLE categories(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    parent_id BIGINT,
    name            VARCHAR(255)     NOT NULL,
    image_url   VARCHAR(255),
    enabled    BOOLEAN  DEFAULT TRUE,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attributes(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    name varchar(255),
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attribute_values(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    attribute_id BIGINT     NOT NULL,
    value VARCHAR(255),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    name            VARCHAR(255)     NOT NULL,
    category_id       BIGINT     NOT NULL,
    description TEXT,
    published BOOLEAN  DEFAULT FALSE,
    deleted BOOLEAN  DEFAULT FALSE,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_images(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    product_id       BIGINT     NOT NULL,
    image_type varchar(30),
    image_url varchar(255),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_attributes(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_id       BIGINT     NOT NULL,
    attribute_value_id BIGINT     NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_variants(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_id       BIGINT     NOT NULL,
    variant_name VARCHAR(255)     NOT NULL,
    cost double precision,
    price double precision,
    stock SMALLINT,
    deleted    BOOLEAN  DEFAULT FALSE,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_variant_images(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    product_variant_id       BIGINT     NOT NULL,
    image_url varchar(255),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_variant_options(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_variant_id  BIGINT     NOT NULL,
    attribute_value_id BIGINT     NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wishlist(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_id BIGINT   NOT NULL,
    user_id BIGINT   NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

