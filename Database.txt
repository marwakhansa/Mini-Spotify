-- Create database (no DROP to preserve data)
CREATE DATABASE IF NOT EXISTS userregistration;
USE userregistration;
-- -------------------------------------------------------------------
-- 1. USER TABLE (For Login and Registration)
-- -------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    -- NOTE: Keep in mind that for security, this column should store a password hash,
    -- not plain text. For now, it matches your code's definition.
    password VARCHAR(100),
    country VARCHAR(50),
    gender VARCHAR(20)
);
-- Insert sample users
INSERT INTO user (name, email, username, password, country, gender) VALUES
('Test User', 'test@example.com', 'test123', '12345', 'Pakistan', 'Male'),
('Sana', 'sana@gmail.com', 'Sana', '123', 'Pakistan', 'Female')
ON DUPLICATE KEY UPDATE username=username; -- Syntax to prevent insert failure if table exists
-- -------------------------------------------------------------------
-- 2. SONG TABLE
-- -------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS song (
    id INT AUTO_INCREMENT PRIMARY KEY,
    SongName VARCHAR(255),
    Artist VARCHAR(255),
    Genre VARCHAR(100),
    Duration VARCHAR(20)
    -- file_path will be added below
);
-- Insert sample songs (MUST come before the UPDATE statements)
INSERT INTO song (SongName, Artist, Genre, Duration) VALUES
('Faded', 'Alan Walker', 'Electronic', '3:32'),
('Shape of You', 'Ed Sheeran', 'Pop', '4:24'),
('Believer', 'Imagine Dragons', 'Rock', '3:24'),
('On My Way', 'Alan Walker', 'Pop', '3:14'),
('in dinon', 'atif aslam', 'country', '3:14')
ON DUPLICATE KEY UPDATE SongName=SongName;
-- **Corrected ALTER TABLE syntax:**
-- The keyword 'to column 6' is removed. Using 'ADD COLUMN' adds it to the end by default.
ALTER TABLE song
ADD COLUMN  file_path VARCHAR(255);
ALTER TABLE song
ADD COLUMN  image_path VARCHAR(255);
-- Temporarily turn off safe update mode to allow mass updates based on SongName
SET SQL_SAFE_UPDATES = 0;
-- Example updates with paths
UPDATE song SET file_path='music/Faded.mp3', image_path='images/Faded.jpg' WHERE SongName='Faded';
UPDATE song SET file_path='music/Shape of You.mp3', image_path='images/Shape of You.jpg' WHERE SongName='Shape of You';
UPDATE song SET file_path='music/Believer.mp3', image_path='images/Believer.jpg' WHERE SongName='Believer';
UPDATE song SET file_path='music/On My Way.mp3', image_path='images/On My Way.jpg' WHERE SongName='On My Way';
UPDATE song SET file_path='music/in dinon.mp3', image_path='images/in dinon.jpg' WHERE SongName='in dinon';
UPDATE song SET file_path='music/Afsos.mp3', image_path='images/in dinon.jpg' WHERE SongName='afsoos';
-- Re-enable safe update mode (recommended)
SET SQL_SAFE_UPDATES = 1;
-- -------------------------------------------------------------------
-- 3. PLAYLIST TABLE
-- -------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS playlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    song_id INT,
    user_id INT,  -- Added this column to associate playlists with users
    FOREIGN KEY (song_id) REFERENCES song(id),
    FOREIGN KEY (user_id) REFERENCES user(id),  -- Added foreign key for user_id
    UNIQUE KEY (user_id, song_id)  -- Optional but recommended: Prevents duplicate songs per user in playlist
);
-- Check data
SELECT * FROM user;
SELECT * FROM song;
SELECT * FROM playlist;
INSERT INTO song (SongName, Artist, Genre, Duration) VALUES
('Faded', 'Alan Walker', 'Electronic', '3:32'),
('Shape of You', 'Ed Sheeran', 'Pop', '4:24'),
('Believer', 'Imagine Dragons', 'Rock', '3:24'),
('On My Way', 'Alan Walker', 'Pop', '3:14'),
('in dinon', 'atif aslam', 'country', '3:14'),
('afsoos','atif','pop','3:21')
ON DUPLICATE KEY UPDATE SongName=SongName;

ALTER TABLE playlist ADD COLUMN user_id INT;
ALTER TABLE playlist ADD FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE playlist ADD UNIQUE KEY (user_id, song_id);  -- Optional, but prevents duplicates