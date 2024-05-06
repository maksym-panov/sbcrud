ALTER TABLE sbc_schema.book ADD COLUMN quantity INTEGER NOT NULL DEFAULT 0;

ALTER TABLE sbc_schema.book ADD CONSTRAINT not_negative_quantity CHECK ( book.quantity >= 0 );