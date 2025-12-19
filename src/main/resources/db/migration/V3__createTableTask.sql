CREATE TABLE Task (
    id BIGINT NOT NULL AUTO_INCREMENT,
    statement TEXT NOT NULL,
    order_number INT,
    course_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES Course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;