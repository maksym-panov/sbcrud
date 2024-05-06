CREATE TABLE sbc_schema.Favourite(
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,

    CONSTRAINT favourite_pk PRIMARY KEY (user_id, book_id)
);