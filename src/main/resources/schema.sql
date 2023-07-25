/*
Только для работы с postman, не пушить!
*/
ALTER TABLE FRIENDS
DROP
CONSTRAINT FK_FRIENDS_F;
ALTER TABLE FRIENDS
DROP
CONSTRAINT FK_FRIENDS_S;
ALTER TABLE LIKES
DROP
CONSTRAINT FK_LIKES_U;
ALTER TABLE LIKES
DROP
CONSTRAINT FK_LIKES_F;
ALTER TABLE FILM_GENRES
DROP
CONSTRAINT FK_FILM_GENRES_F;
ALTER TABLE FILM_GENRES
DROP
CONSTRAINT FK_FILM_GENRES_G;
ALTER TABLE MPA_FILMS
DROP
CONSTRAINT FK_MPA_FILMS_F;
ALTER TABLE MPA_FILMS
DROP
CONSTRAINT FK_MPA_FILMS_M;

DROP TABLE FRIENDS;
DROP TABLE FILM_GENRES;
DROP TABLE LIKES;
DROP TABLE MPA_CATEGORIES;
DROP TABLE GENRES;
DROP TABLE FILMS;
DROP TABLE USERS;
DROP TABLE MPA_FILMS;
/*
Дальше нужно пушить.
*/
CREATE TABLE IF NOT EXISTS USERS
(
    user_id
    integer
    NOT
    NULL
    PRIMARY
    KEY
    AUTO_INCREMENT,
    user_login
    varchar
    NOT
    NULL,
    user_name
    varchar,
    email
    varchar
    NOT
    NULL,
    birthday
    timestamp
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS MPA_CATEGORIES
(
    mpa_category_id
    integer
    NOT
    NULL
    PRIMARY
    KEY
    AUTO_INCREMENT,
    category_name
    varchar
);

CREATE TABLE IF NOT EXISTS FILMS
(
    film_id
    integer
    NOT
    NULL
    PRIMARY
    KEY
    AUTO_INCREMENT,
    film_name
    varchar
    NOT
    NULL,
    film_description
    varchar
    NOT
    NULL,
    release_date
    timestamp
    NOT
    NULL,
    duration
    integer
    NOT
    NULL
);

CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id
    integer
    NOT
    NULL
    PRIMARY
    KEY
    AUTO_INCREMENT,
    genre_name
    varchar
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    film_id integer NOT NULL CONSTRAINT FK_FILM_GENRES_F REFERENCES FILMS
(
    film_id
),
    genre_id integer NOT NULL CONSTRAINT FK_FILM_GENRES_G REFERENCES GENRES
(
    genre_id
),
    PRIMARY KEY
(
    film_id,
    genre_id
)
    );

CREATE TABLE IF NOT EXISTS LIKES
(
    user_id integer NOT NULL CONSTRAINT FK_LIKES_U REFERENCES USERS
(
    user_id
),
    film_id integer NOT NULL CONSTRAINT FK_LIKES_F REFERENCES FILMS
(
    film_id
),
    PRIMARY KEY
(
    film_id,
    user_id
)
    );

CREATE TABLE IF NOT EXISTS MPA_FILMS
(
    film_id integer NOT NULL CONSTRAINT FK_MPA_FILMS_F REFERENCES FILMS
(
    film_id
),
    mpa_category_id integer NOT NULL CONSTRAINT FK_MPA_FILMS_M REFERENCES MPA_CATEGORIES
(
    mpa_category_id
),
    PRIMARY KEY
(
    film_id,
    mpa_category_id
)
    );

CREATE TABLE IF NOT EXISTS FRIENDS
(
    user_id integer NOT NULL CONSTRAINT FK_FRIENDS_F REFERENCES USERS
(
    user_id
),
    friend_id integer NOT NULL CONSTRAINT FK_FRIENDS_S REFERENCES USERS
(
    user_id
),
    PRIMARY KEY
(
    user_id,
    friend_id
)
    );
