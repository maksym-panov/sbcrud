INSERT INTO sbc_schema.service_user(id, email, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'admin@maksympanov.com', NULL, 'Admin', 'Admin', 'ADMIN', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sbc_schema.service_user(id, email, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'vendor@maksympanov.com', NULL, 'Vendor', 'Vendor', 'VENDOR', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sbc_schema.service_user(id, email, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'user@maksympanov.com', NULL, 'User', 'User', 'USER', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
