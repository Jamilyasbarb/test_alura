CREATE TABLE H2_USER (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'STUDENT', -- substituir enum por VARCHAR
    password VARCHAR(20) NOT NULL,
    CONSTRAINT UC_Email UNIQUE (email)
);

CREATE TABLE Course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    instructor_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'BUILDING', -- substituir enum por VARCHAR
    publishedAt TIMESTAMP DEFAULT NULL,
    CONSTRAINT FK_Author FOREIGN KEY (instructor_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    statement TEXT NOT NULL,
    order_number INT,
    type INT,
    course_id BIGINT,
    CONSTRAINT FK_Course FOREIGN KEY (course_id) REFERENCES Course(id)
);

CREATE TABLE TaskOption (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    option_text VARCHAR(255) NOT NULL,
    isCorrect BOOLEAN NOT NULL,  -- substituir TINYINT(1) por BOOLEAN
    task_id BIGINT NOT NULL,
    CONSTRAINT fk_task_option_task
        FOREIGN KEY (task_id)
        REFERENCES Task(id)
);