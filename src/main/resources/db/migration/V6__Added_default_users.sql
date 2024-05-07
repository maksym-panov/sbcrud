INSERT INTO sbc_schema.service_user(id, email, password_hash, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'admin@maksympanov.com', '$2a$10$ryyM5fcgA9iM5dv7qwbHp.y1loQ1W0329C9MjuDfzFWdfmmvQXyGO', NULL, 'Admin', 'Admin', 'ADMIN', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sbc_schema.service_user(id, email, password_hash, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'vendor@maksympanov.com', '$2a$10$ryyM5fcgA9iM5dv7qwbHp.y1loQ1W0329C9MjuDfzFWdfmmvQXyGO', NULL, 'Vendor', 'Vendor', 'VENDOR', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sbc_schema.service_user(id, email, password_hash, phone_number, first_name, last_name, role, version, date_created, date_modified)
    VALUES (gen_random_uuid(), 'user@maksympanov.com', '$2a$10$ryyM5fcgA9iM5dv7qwbHp.y1loQ1W0329C9MjuDfzFWdfmmvQXyGO', NULL, 'User', 'User', 'USER', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
