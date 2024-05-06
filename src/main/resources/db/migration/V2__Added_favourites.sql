CREATE TABLE sbc_schema.favourite(
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    version INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_modified TIMESTAMP NOT NULL,

    CONSTRAINT favourite_pk PRIMARY KEY (id)
);