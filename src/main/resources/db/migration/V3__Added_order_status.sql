ALTER TABLE sbc_schema.customer_order ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'CREATED';

ALTER TABLE sbc_schema.customer_order ADD CONSTRAINT status_values
    CHECK ( customer_order.status IN ( 'CREATED', 'PROCESSING', 'SHIPPING', 'DELIVERED', 'CANCELLED' ) );