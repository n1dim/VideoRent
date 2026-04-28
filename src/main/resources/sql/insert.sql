-- USERS
INSERT INTO users (telephone_number, full_name, home_address, password_hash, role, is_blocked) VALUES
    ('+358401234567', 'Иван Петров', 'Helsinki, Mannerheimintie 10', 'hash1', 'USER', FALSE),
    ('+358401234568', 'Анна Смирнова', 'Espoo, Tapiola 5', 'hash2', 'USER', FALSE),
    ('+358401234569', 'Олег Иванов', 'Vantaa, Tikkurila 3', 'hash3', 'USER', TRUE),
    ('+358401234570', 'Администратор', 'Helsinki, Admin street 1', 'admin_hash', 'ADMIN', FALSE);

-- MOVIES
INSERT INTO movies (title, film_director, description, company, release_year) VALUES
    ('Inception', 'Christopher Nolan', 'A thief who steals corporate secrets through dreams.', 'Warner Bros', 2010),
    ('The Matrix', 'Wachowski Sisters', 'A hacker discovers the reality is a simulation.', 'Warner Bros', 1999),
    ('Interstellar', 'Christopher Nolan', 'Explorers travel through a wormhole in space.', 'Paramount Pictures', 2014),
    ('The Godfather', 'Francis Ford Coppola', 'The aging patriarch transfers control of his empire.', 'Paramount Pictures', 1972),
    ('Titanic', 'James Cameron', 'A love story on the ill-fated ship.', '20th Century Fox', 1997);

-- MEDIA TYPES
INSERT INTO media (name) VALUES
    ('DVD'),
    ('Blu-ray'),
    ('Digital');

-- COPIES
INSERT INTO copies (film_id, media_id, cost, count, status) VALUES
    (1, 1, 5, 10, 'AVAILABLE'), -- Inception DVD
    (1, 2, 7, 5, 'AVAILABLE'),  -- Inception Blu-ray
    (2, 1, 4, 8, 'AVAILABLE'),  -- Matrix DVD
    (3, 2, 8, 6, 'AVAILABLE'),  -- Interstellar Blu-ray
    (4, 1, 6, 3, 'UNAVAILABLE'),-- Godfather DVD
    (5, 3, 3, 20, 'AVAILABLE'); -- Titanic Digital

-- RENTALS (история проката)
INSERT INTO rentals (user_id, copy_id, issue_date, due_date, return_date, extension_requested, requested_date) VALUES
    (1, 1, '2026-04-01', '2026-04-10', '2026-04-09', FALSE, NULL),
    (2, 2, '2026-04-05', '2026-04-15', NULL, TRUE, '2026-04-14'),
    (1, 3, '2026-03-20', '2026-03-30', '2026-03-29', FALSE, NULL),
    (3, 4, '2026-04-10', '2026-04-20', NULL, FALSE, NULL),
    (2, 6, '2026-04-12', '2026-04-22', '2026-04-21', FALSE, NULL);