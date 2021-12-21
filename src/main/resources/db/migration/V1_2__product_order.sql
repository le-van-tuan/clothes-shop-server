create TABLE orders(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    user_id BIGINT     NOT NULL,
    email VARCHAR(255),
    name VARCHAR(255),
    phone_number VARCHAR(255),
    city VARCHAR(255),
    district varchar(255),
    ward varchar(255),
    address varchar(255),
    sub_total double precision,
    shipping_fee double precision,
    total   double precision,
    order_number       VARCHAR(30),
    order_status    VARCHAR(30),
    message TEXT,
    updated_at      TIMESTAMP,
    updated_by BIGINT,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE orders ADD CONSTRAINT fk_order_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE NO ACTION;

create TABLE order_items(
    id  SERIAL  NOT NULL    PRIMARY KEY,
    order_id BIGINT     NOT NULL,
    product_id BIGINT     NOT NULL,
    variant_id BIGINT     NOT NULL,
    quantity    SMALLINT,
    unit_price  double precision,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE NO ACTION;