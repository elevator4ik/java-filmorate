package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AllFilmsGenresExtractor implements ResultSetExtractor<Map<Integer, List<Genre>>> {

    @Override
    public Map<Integer, List<Genre>> extractData(ResultSet rs) throws SQLException {
        Map<Integer, List<Genre>> data = new HashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("FILM_ID");
            data.putIfAbsent(filmId, new ArrayList<>());
            Genre genre = new Genre(rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME"));
            data.get(filmId).add(genre);
        }
        return data;
    }
}