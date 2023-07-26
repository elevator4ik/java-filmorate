package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MpaFilmExtractor implements ResultSetExtractor<Map<Integer, Mpa>> {

    @Override
    public Map<Integer, Mpa> extractData(ResultSet rs) throws SQLException {
        Map<Integer, Mpa> data = new HashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("FILM_ID");
            Mpa mpa = new Mpa(rs.getInt("MPA_CATEGORY_ID"), rs.getString("CATEGORY_NAME"));
            data.put(filmId, mpa);
        }
        return data;
    }
}