CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    telephone_number VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    home_address VARCHAR(200) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('USER', 'ADMIN')) DEFAULT 'USER',
    is_blocked BOOLEAN DEFAULT FALSE
);

CREATE TABLE movies (
    film_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    film_director VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    company VARCHAR(100) NOT NULL,
    release_year INT NOT NULL
);

CREATE TABLE media (
    media_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE copies (
    copy_id BIGSERIAL PRIMARY KEY,
    film_id BIGINT REFERENCES movies(film_id),
    media_id BIGINT REFERENCES media(media_id),
    cost INT NOT NULL,
    count INT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('AVAILABLE', 'UNAVAILABLE'))
);

CREATE TABLE rentals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id),
    copy_id BIGINT REFERENCES copies(copy_id),
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    extension_requested BOOLEAN DEFAULT FALSE,
    requested_date DATE
);
