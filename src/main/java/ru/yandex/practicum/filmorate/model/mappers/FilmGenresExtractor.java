package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmGenresExtractor implements ResultSetExtractor<List<Genre>> {

    @Override
    public List<Genre> extractData(ResultSet rs) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("GENRE_ID"),
                    rs.getString("GENRE_NAME")));
        }
        return genres;
    }
}