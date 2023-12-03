INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, "admin@bookstore.ua", "$2a$10$bDpYvJC1c/ZQjPnk2TucHOydv18Jo.81poZo/5bM4V1zavBrfL7Y2", "Bob", "Bobson", "" , false);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (2, "user@bookstore.ua", "$2a$10$I5KuzQSeQy3nmLep3plrz.M.fF3g1x.6pcj0SHUzWjACZVvhrIQrO", "Alice", "Anderson", "" , false);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
