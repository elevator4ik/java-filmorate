package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.mappers.GenreRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addGenre(Genre genre) {
        final String sqlQuery = "INSERT INTO GENRES (GENRE_ID, GENRE_NAME) " +
                "VALUES (:genreId, :name) ";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("genreId", genre.getId());
        map.addValue("name", genre.getName());
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public Genre getGenre(int genreId) {

        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = :genreId ";
        List<Genre> list = jdbcOperations.query(sqlQuery, Map.of("genreId", genreId), new GenreRowMapper());

        if (list.size() != 1) {
            throw new NotFoundException("Something wrong! Count of genres is " + list.size());
        }
        return list.get(0);
    }

    @Override
    public List<Genre> getGenres() {

        final String sqlQuery = "SELECT * FROM GENRES";
        return jdbcOperations.query(sqlQuery, new GenreRowMapper());
    }

    @Override
    public List<Genre> findByIds(List<Integer> ids) {

        final String sqlQuery = "SELECT * FROM GENRES";
        List<Genre> list = jdbcOperations.query(sqlQuery, new GenreRowMapper());
        List<Genre> genres = new ArrayList<>();
        for (Genre g : list) {
            if (ids.contains(g.getId())) {
                genres.add(g);
            }
        }
        return genres;
    }
}
