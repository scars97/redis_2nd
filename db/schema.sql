CREATE TABLE IF NOT EXISTS movie_entity (
    release_date DATE,
    run_time INTEGER NOT NULL,
    create_at DATETIME(6),
    modified_at DATETIME(6),
    movie_id BIGINT NOT NULL AUTO_INCREMENT,
    create_by VARCHAR(255),
    genre VARCHAR(255),
    modified_by VARCHAR(255),
    rating VARCHAR(255),
    thumbnail VARCHAR(255),
    title VARCHAR(255),
    PRIMARY KEY (movie_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_movie_release_date ON movie_entity (release_date);
CREATE INDEX idx_movie_genre ON movie_entity (genre);
CREATE FULLTEXT INDEX idx_movie_title ON movie_entity(title);

CREATE TABLE IF NOT EXISTS theater_schedule_entity (
    end_time TIME(6),
    screening_date DATE,
    start_time TIME(6),
    create_at DATETIME(6),
    modified_at DATETIME(6),
    movie_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL AUTO_INCREMENT,
    theater_id BIGINT NOT NULL,
    create_by VARCHAR(255),
    modified_by VARCHAR(255),
    PRIMARY KEY (schedule_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_schedule_movie_id ON theater_schedule_entity (movie_id);

CREATE TABLE IF NOT EXISTS theater_entity (
    create_at DATETIME(6),
    modified_at DATETIME(6),
    theater_id BIGINT NOT NULL AUTO_INCREMENT,
    create_by VARCHAR(255),
    modified_by VARCHAR(255),
    name VARCHAR(255),
    PRIMARY KEY (theater_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS reservation_entity (
    create_at DATETIME(6),
    modified_at DATETIME(6),
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    create_by VARCHAR(255),
    modified_by VARCHAR(255),
    status ENUM('DONE'),
    PRIMARY KEY (reservation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS seat_entity (
    create_at DATETIME(6),
    modified_at DATETIME(6),
    schedule_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL AUTO_INCREMENT,
    create_by VARCHAR(255),
    modified_by VARCHAR(255),
    seat_number VARCHAR(255),
    status ENUM('AVAILABLE', 'RESERVED'),
    PRIMARY KEY (seat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_entity (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;