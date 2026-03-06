CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    telephone_number VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    home_address VARCHAR(200) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('user', 'admin')) DEFAULT 'user',
    is_blocked BOOLEAN DEFAULT FALSE
);

CREATE TABLE movies (
    film_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    film_director VARCHAR(100),
    description TEXT,
    company VARCHAR(100),
    release_year INT NOT NULL
);

CREATE TABLE media (
    media_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE copies (
    copy_id SERIAL PRIMARY KEY,
    film_id INT REFERENCES movies(film_id),
    media_id INT REFERENCES media(media_id),
    cost INT NOT NULL,
    count INT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('available', 'unavailable'))
);

CREATE TABLE rentals (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    copy_id INT REFERENCES copies(copy_id),
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    extension_requested BOOLEAN DEFAULT FALSE,
    requested_date DATE
);
