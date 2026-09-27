INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(1, 'admin', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO user(id, name, password, role, created_at, updated_at, created_by, updated_by, version) 
VALUES(2, 'user', '{bcrypt}$2a$10$dviiOZlbvIyWQiYM3pWEy.sgwZ7n30mmWOOl1hzP6RQJ9M92u.e5m', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);

INSERT INTO business (id, name, genre, status, overview, created_at, updated_at, created_by, updated_by, version)
VALUES (1, 'Business A', 'SAAS', 'IDEA', '概要A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO business (id, name, genre, status, overview, created_at, updated_at, created_by, updated_by, version)
VALUES (2, 'Business B', 'ECOMMERCE', 'OPERATING', '概要B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2, 0);

INSERT INTO business_model_canvas (business_id, customer_segments, value_proposition, channels, customer_relationships, revenue_streams, key_resources, key_activities, key_partners, cost_structure, created_at, updated_at, created_by, updated_by, version)
VALUES (1, '顧客A', '価値A', 'Web', 'メール', '月額', '技術', '開発', '業者', '人件費', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 0);
INSERT INTO business_model_canvas (business_id, customer_segments, value_proposition, channels, customer_relationships, revenue_streams, key_resources, key_activities, key_partners, cost_structure, created_at, updated_at, created_by, updated_by, version)
VALUES (2, '顧客B', '価値B', '店頭', '口コミ', '販売', '在庫', '物流', 'パートナー', '配送', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2, 2, 0);
