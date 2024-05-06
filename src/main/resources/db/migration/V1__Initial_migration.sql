CREATE SCHEMA sbc_schema;

CREATE TABLE ServiceUser(
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(30),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT service_user_pk PRIMARY KEY (id),
    CONSTRAINT email_ak UNIQUE (email),
    CONSTRAINT user_role_values CHECK (role IN ( 'USER', 'VENDOR', 'ADMIN' ) )
);

CREATE TABLE Genre(
    id UUID NOT NULL,
    genre_name VARCHAR(255) NOT NULL,
    description TEXT,
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT genre_pk PRIMARY KEY (id),
    CONSTRAINT genre_name_ak UNIQUE (genre_name)
);

CREATE TABLE Book(
    id UUID NOT NULL,
    isbn VARCHAR(31) NOT NULL,
    book_name VARCHAR(255) NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    description TEXT,
    image_uri VARCHAR(255),
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT book_pk PRIMARY KEY (id),
    CONSTRAINT isbn_ak UNIQUE (isbn)
);

CREATE TABLE BookGenre(
    book_id UUID NOT NULL,
    genre_id UUID NOT NULL,
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT book_genre_pk PRIMARY KEY (book_id, genre_id),
    CONSTRAINT book_fk FOREIGN KEY (book_id) REFERENCES Book,
    CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES Genre
);

CREATE TABLE CustomerOrder(
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT customer_order_pk PRIMARY KEY (id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES ServiceUser
);

CREATE TABLE OrderItem(
    order_id UUID NOT NULL,
    book_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    version INTEGER NOT NULL,
    dateCreated TIMESTAMP NOT NULL,
    dateModified TIMESTAMP NOT NULL,

    CONSTRAINT order_item_pk PRIMARY KEY (order_id, book_id),
    CONSTRAINT positive_quantity CHECK ( quantity > 0 )
);