package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.MPACategoryRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.mappers.MpaFilmExtractor;
import ru.yandex.practicum.filmorate.model.mappers.MpaRowMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcMPACategoryRepository implements MPACategoryRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addMpaCategory(Mpa newMpa) {
        final String sqlQuery = "INSERT INTO MPA_CATEGORIES (MPA_CATEGORY_ID, CATEGORY_NAME) " +
                "VALUES (:mpaId, :name) ";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("mpaId", newMpa.getId());
        map.addValue("name", newMpa.getName());
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public Mpa getMpaCategory(int mpaId) {

        final String sqlQuery = "SELECT * " +
                "FROM MPA_CATEGORIES " +
                "WHERE MPA_CATEGORY_ID = :mpaId ";
        List<Mpa> list = jdbcOperations.query(sqlQuery, Map.of("mpaId", mpaId), new MpaRowMapper());

        if (list.size() != 1) {
            throw new NotFoundException("Something wrong! Count of MPAs is " + list.size());
        }
        return list.get(0);
    }

    @Override
    public Map<Integer, Mpa> getAllFilmsMpaCategories() {

        final String sqlQuery = "SELECT M.FILM_ID, " +
                "C.MPA_CATEGORY_ID, " +
                "C.CATEGORY_NAME " +
                "FROM MPA_FILMS AS M " +
                "JOIN MPA_CATEGORIES AS C ON C.MPA_CATEGORY_ID = M.MPA_CATEGORY_ID";
        return jdbcOperations.query(sqlQuery, new MpaFilmExtractor());
    }

    @Override
    public Mpa getFilmMpaCategory(int filmId) {

        final String sqlQuery = "SELECT C.MPA_CATEGORY_ID, " +
                "C.CATEGORY_NAME " +
                "FROM MPA_CATEGORIES AS C " +
                "JOIN MPA_FILMS AS M ON C.MPA_CATEGORY_ID = M.MPA_CATEGORY_ID " +
                "WHERE M.FILM_ID = :filmId";
        List<Mpa> list = jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new MpaRowMapper());
        if (list.size() != 1) {
            throw new NotFoundException("Something wrong! Count of MPAs is " + list.size());
        }
        return list.get(0);
    }

    @Override
    public List<Mpa> getMpaCategories() {

        final String sqlQuery = "SELECT * FROM MPA_CATEGORIES";
        return jdbcOperations.query(sqlQuery, new MpaRowMapper());
    }
}
