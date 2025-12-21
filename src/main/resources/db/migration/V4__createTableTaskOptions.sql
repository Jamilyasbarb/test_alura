CREATE TABLE TaskOption (
    id BIGINT NOT NULL AUTO_INCREMENT,
    option_text VARCHAR(255) NOT NULL,
    isCorrect TINYINT(1) NOT NULL,
    task_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_task_option_task
        FOREIGN KEY (task_id)
        REFERENCES task(id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;