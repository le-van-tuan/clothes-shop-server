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
ALTER TABLE attribute_values ADD CONSTRAINT fk_attribute_values FOREIGN KEY (attribute_id) REFERENCES attributes(id) ON DELETE NO ACTION;

CREATE TABLE products(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    name            VARCHAR(255)     NOT NULL,
    category_id       BIGINT     NOT NULL,
    description TEXT,
    published BOOLEAN  DEFAULT FALSE,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE products ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE NO ACTION;

CREATE TABLE product_images(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    product_id       BIGINT     NOT NULL,
    image_type varchar(30),
    image_url varchar(255),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_images ADD CONSTRAINT fk_product_image_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION;

CREATE TABLE product_attributes(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_id       BIGINT     NOT NULL,
    attribute_value_id BIGINT     NOT NULL,
    created_at      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_attributes ADD CONSTRAINT fk_product_attribute_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION;
ALTER TABLE product_attributes ADD CONSTRAINT fk_product_attribute_attribute_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_values(id) ON DELETE NO ACTION;

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
ALTER TABLE product_variants ADD CONSTRAINT fk_product_variants_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION;

CREATE TABLE product_variant_images(
    id              SERIAL           NOT NULL      PRIMARY KEY,
    product_variant_id       BIGINT     NOT NULL,
    image_url varchar(255),
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_variant_images ADD CONSTRAINT fk_product_variant_images_product_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE NO ACTION;

CREATE TABLE product_variant_options(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_variant_id  BIGINT     NOT NULL,
    attribute_value_id BIGINT     NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE product_variant_options ADD CONSTRAINT fk_product_variant_options_product_variants FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE NO ACTION;
ALTER TABLE product_variant_options ADD CONSTRAINT fk_product_variant_options_attribute_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_values(id) ON DELETE NO ACTION;

CREATE TABLE wishlist(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    product_id BIGINT   NOT NULL,
    user_id BIGINT   NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE wishlist ADD CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION;
ALTER TABLE wishlist ADD CONSTRAINT fk_wishlist_user FOREIGN KEY (product_id) REFERENCES users(id) ON DELETE NO ACTION;

