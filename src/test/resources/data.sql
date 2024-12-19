INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name1', 'email1', 'password', 'USER', 'NORMAL');
INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name2', 'email2', 'password', 'USER', 'NORMAL');
INSERT INTO app_user (nickname, email, password, role, status)
VALUES ('name3', 'email3', 'password', 'ADMIN', 'NORMAL');

INSERT INTO item (name, description, owner_id, manager_id, status)
VALUES ('name1', 'description1', 1, 1, 'PENDING');
INSERT INTO item (name, description, owner_id, manager_id, status)
VALUES ('name2', 'description2', 2, 2, 'PENDING');

INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 2, CURRENT_DATE, CURRENT_DATE, 'PENDING');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (2, 2, CURRENT_DATE, CURRENT_DATE, 'APPROVED');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 1, NOW(), NOW(), 'EXPIRED');
INSERT INTO reservation (item_id, user_id, start_at, end_at, status)
VALUES (1, 1, NOW(), NOW(), 'CANCELED');