CREATE SCHEMA IF NOT EXISTS sbc_schema;

CREATE TABLE sbc_schema.service_user(
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(30),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_modified TIMESTAMP NOT NULL,

    CONSTRAINT service_user_pk PRIMARY KEY (id),
    CONSTRAINT email_ak UNIQUE (email),
    CONSTRAINT user_role_values CHECK (role IN ( 'USER', 'VENDOR', 'ADMIN' ) )
);

CREATE TABLE sbc_schema.genre(
    id UUID NOT NULL,
    genre_name VARCHAR(255) NOT NULL,
    description TEXT,
    version INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_modified TIMESTAMP NOT NULL,

    CONSTRAINT genre_pk PRIMARY KEY (id),
    CONSTRAINT genre_name_ak UNIQUE (genre_name)
);

CREATE TABLE sbc_schema.book(
    id UUID NOT NULL,
    isbn VARCHAR(31) NOT NULL,
    book_name VARCHAR(255) NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    description TEXT,
    image_uri VARCHAR(255),
    version INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_modified TIMESTAMP NOT NULL,

    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT isbn_ak UNIQUE (isbn)
);

CREATE TABLE sbc_schema.book_genre(
    id UUID NOT NULL,
    book_id UUID NOT NULL,
    genre_id UUID NOT NULL,

    CONSTRAINT book_genre_pk PRIMARY KEY (id),
    CONSTRAINT book_fk FOREIGN KEY (book_id) REFERENCES book,
    CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre
);

CREATE TABLE sbc_schema.customer_order(
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    version INTEGER NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_modified TIMESTAMP NOT NULL,

    CONSTRAINT customer_order_pk PRIMARY KEY (id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES service_user
);

CREATE TABLE sbc_schema.order_book(
    id UUID NOT NULL,
    order_id UUID NOT NULL,
    book_id UUID NOT NULL,
    quantity INTEGER NOT NULL,

    CONSTRAINT order_item_pk PRIMARY KEY (id),
    CONSTRAINT positive_quantity CHECK ( quantity > 0 )
);