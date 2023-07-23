package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmGenreRepository;
import ru.yandex.practicum.filmorate.model.mappers.GenreIdRowMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcFilmGenreRepository implements FilmGenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addGenreToFilm(int filmId, int genreId) {
        final String sqlQuery = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
                "VALUES (:filmId, :genreId) ";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("genreId", genreId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void deleteGenreFromFilm(int filmId, int genreId) {
        final String sqlQuery = "DELETE FROM FILM_GENRES " +
                "WHERE FILM_ID = :filmId " +
                "AND GENRE_ID = :genreId";
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("genreId", genreId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public List<Integer> getFilmGenres(int filmId) {

        final String sqlQuery = "SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = :filmId ";
        return jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new GenreIdRowMapper());
    }
}
