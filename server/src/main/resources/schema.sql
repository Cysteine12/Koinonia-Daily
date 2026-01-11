-- DROP TABLE tokens;
CREATE TABLE IF NOT EXISTS test_table (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
-- DROP TABLE IF EXISTS histories;
-- DROP TABLE IF EXISTS bookmarks;
-- DROP TABLE IF EXISTS bookmark_categories;
-- DROP TABLE IF EXISTS tokens;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS collection_teachings;
-- DROP TABLE IF EXISTS teachings;
-- DROP TABLE IF EXISTS series CASCADE;

-- ALTER TABLE series RENAME COLUMN title TO name;