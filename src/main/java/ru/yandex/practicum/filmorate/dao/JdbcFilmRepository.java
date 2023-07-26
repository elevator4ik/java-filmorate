package ru.yandex.practicum.filmorate.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.mappers.FilmRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
@Slf4j
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    @NonNull//чтобы сработала аннотация коструктора
    private FilmRowMapper filmRowMapper;

    @Override
    public List<Film> getFilms() {

        final String sqlQuery = "SELECT * FROM FILMS";

        log.info("Get all films.");
        return jdbcOperations.query(sqlQuery, filmRowMapper);
    }

    @Override
    public Film getFilm(int filmId) {

        final String sqlQuery = "SELECT * FROM FILMS WHERE FILM_ID = :filmId ";
        final List<Film> films = jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), filmRowMapper);

        if (films.size() != 1) {
            log.info("Something wrong! Count of films is {}", films.size());
            throw new NotFoundException("Something wrong! Count of films is " + films.size());
        }

        log.info("Get film with id {}", filmId);
        return films.get(0);
    }

    @Override
    public Film add(Film film) {
        final String sqlQuery = "INSERT INTO FILMS (FILM_NAME, " +
                "FILM_DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION) " +
                "VALUES (:name, :description, :releaseDate, :duration) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        jdbcOperations.update(sqlQuery, map, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        log.info("Put film with id {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {

        final String sqlQuery = "UPDATE FILMS " +
                "SET FILM_NAME = :name, " +
                "FILM_DESCRIPTION = :description, " +
                "RELEASE_DATE = :releaseDate, " +
                "DURATION = :duration " +
                "WHERE FILM_ID = :filmId";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", film.getId());
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        jdbcOperations.update(sqlQuery, map);

        log.info("Updated film with id {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getFilmsByLikes(int count) {

        final String sqlQuery = "SELECT * " +
                "FROM FILMS " +
                "WHERE FILM_ID IN (SELECT O.FILM_ID " +
                "FROM (SELECT FILM_ID, COUNT(USER_ID) AS COUNT " +
                "FROM LIKES " +
                "GROUP BY FILM_ID " +
                "ORDER BY COUNT DESC " +
                "LIMIT :count) AS O)";

        log.info("Get films sorted by likes.");
        return jdbcOperations.query(sqlQuery, Map.of("count", count), filmRowMapper);
    }

}
