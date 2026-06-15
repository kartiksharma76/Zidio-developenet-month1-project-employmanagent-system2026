CREATE TABLE user_auth (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50) NOT NULL,
    department VARCHAR(255),
    designation VARCHAR(255),
    joining_date DATE,
    salary DOUBLE,
    user_auth_id BIGINT,
    CONSTRAINT fk_employee_user_auth FOREIGN KEY (user_auth_id) REFERENCES user_auth (id) ON DELETE CASCADE
);
