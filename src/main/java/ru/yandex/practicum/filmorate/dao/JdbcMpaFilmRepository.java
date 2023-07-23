package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.MpaFilmRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.mappers.MpaFilmRowMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcMpaFilmRepository implements MpaFilmRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addMpaToFilm(Film film) {

        final String sqlQuery = "INSERT INTO MPA_FILMS (FILM_ID, MPA_CATEGORY_ID) " +
                "VALUES (:filmId, :mpaId) ";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("mpaId", film.getMpa().getId());
        map.addValue("filmId", film.getId());
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void deleteMpaFromFilm(Film film) {
        final String sqlQuery = "DELETE FROM MPA_FILMS " +
                "WHERE FILM_ID = :filmId";

        jdbcOperations.update(sqlQuery, Map.of("filmId", film.getId()));
    }

    @Override
    public Integer getFilmMpa(int filmId) {
        final String sqlQuery = "SELECT MPA_CATEGORY_ID FROM MPA_FILMS WHERE FILM_ID = :filmId ";
        List<Integer> list = jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new MpaFilmRowMapper());
        if (list.size() != 0) {
            return list.get(0);
        } else {
            return 0;
        }
    }
}
