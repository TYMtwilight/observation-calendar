-- backend\src\main\resources\db\migration\V2__Insert_sample_data.sql

-- Sample user (パスワードは 'password123' のBCryptハッシュ)
INSERT INTO users (username, email, password_hash, display_name) VALUES
('testuser', 'test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFGjO6NzXy5/7pvBnAI5FIK', 'テストユーザー');

-- Sample observation project
INSERT INTO observation_projects (user_id, title, description, start_date, project_type) VALUES
(1, 'あさがおの観察', '夏休みのあさがおの成長を記録します', '2024-07-20', 'plant');

-- Sample observation records
INSERT INTO observation_records (project_id, observation_date, memo, weather, temperature) VALUES
(1, '2024-07-20', '種をまきました。土が乾いていたので水をたくさんあげました。', 'sunny', 28.5),
(1, '2024-07-21', '昨日と変わりありません。土が少し湿っています。', 'cloudy', 26.0),
(1, '2024-07-22', '小さな芽が出てきました！とてもうれしいです。', 'sunny', 30.0);