package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME")
        );
    }
}
