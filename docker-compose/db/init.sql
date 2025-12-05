DROP DATABASE IF EXISTS aemtAugust1;
CREATE DATABASE IF NOT EXISTS aemtAugust1 CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
USE aemtAugust1;

-- Table des dossiers
CREATE TABLE IF NOT EXISTS folder (
    folder_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    folder_name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    is_deleted_folder BOOLEAN DEFAULT FALSE,
    folder_created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Table des fichiers
CREATE TABLE IF NOT EXISTS file (
    file_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_folder_id BIGINT,
    type ENUM('TEXT', 'IMAGE', 'OTHER') NOT NULL,
    size BIGINT NOT NULL,
    content VARCHAR(255),
    is_deleted_file BOOLEAN DEFAULT FALSE,
    file_created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

ALTER TABLE folder MODIFY folder_name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- Insertion de dossiers
INSERT INTO folder (folder_name, parent_id, folder_created_at) VALUES
('Documents', NULL, '2025-08-10 09:00:00'),
('Images', NULL, '2025-08-10 09:05:00'),
('Projets', NULL, '2025-08-10 09:10:00'),
('Rapports', 1, '2025-08-11 14:00:00'),
('Vacances', 2, '2025-08-11 15:30:00');

-- Insertion de fichiers avec dates différentes
INSERT INTO file (file_name, file_folder_id, type, size, content, file_created_at) VALUES
('resume.txt', 1, 'TEXT', 28, 'Ceci est le contenu du CV...', '2025-08-01 10:00:00'),
('planning.txt', 4, 'TEXT', 28, 'Planning de la semaine : ...', '2025-08-02 11:30:00'),
('plage.png', 5, 'IMAGE', 327373, '/images/plage.png', '2025-08-02 09:15:00'),
('logo.png', 2, 'IMAGE', 1946, '/images/logo.png', '2025-08-03 08:00:00');