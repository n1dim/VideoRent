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
INSERT INTO copies (film_id, media_id, cost, count) VALUES
    (1, 1, 5, 10), -- Inception DVD
    (1, 2, 7, 5),  -- Inception Blu-ray
    (2, 1, 4, 8),  -- Matrix DVD
    (3, 2, 8, 6),  -- Interstellar Blu-ray
    (4, 1, 6, 3),-- Godfather DVD
    (5, 3, 3, 20); -- Titanic Digital

