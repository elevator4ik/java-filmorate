package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.FilmGenreRepository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mappers.AllFilmsGenresExtractor;
import ru.yandex.practicum.filmorate.model.mappers.FilmGenresExtractor;

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
    public List<Genre> getFilmGenres(int filmId) {

        final String sqlQuery = "SELECT G.GENRE_ID, G.GENRE_NAME " +
                "FROM GENRES AS G " +
                "JOIN (SELECT GENRE_ID " +
                "FROM FILM_GENRES " +
                "WHERE FILM_ID = :filmId " +
                ") AS FG ON G.GENRE_ID = FG.GENRE_ID";
        return jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new FilmGenresExtractor());
    }

    @Override
    public Map<Integer, List<Genre>> getAllFilmsGenres() {

        final String sqlQuery = "SELECT FG.FILM_ID, G.GENRE_ID, G.GENRE_NAME " +
                "FROM GENRES AS G " +
                "JOIN FILM_GENRES AS FG ON G.GENRE_ID = FG.GENRE_ID";
        return jdbcOperations.query(sqlQuery, new AllFilmsGenresExtractor());
    }

    @Override
    public Map<Integer, List<Genre>> getFilmGenresByLikes(int count) {

        final String sqlQuery = "SELECT G.GENRE_ID, G.GENRE_NAME " +
                "FROM GENRES AS G " +
                "JOIN (SELECT GENRE_ID " +
                "FROM FILM_GENRES " +
                "WHERE FILM_ID IN (SELECT O.FILM_ID " +
                "FROM (SELECT FILM_ID, COUNT(USER_ID) AS COUNT " +
                "FROM LIKES " +
                "GROUP BY FILM_ID) AS O " +
                "WHERE COUNT <= :count)) AS FG ON G.GENRE_ID = FG.GENRE_ID";
        return jdbcOperations.query(sqlQuery, Map.of("count", count), new AllFilmsGenresExtractor());
    }
}
