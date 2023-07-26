package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_CATEGORY_ID"),
                rs.getString("CATEGORY_NAME"));
    }
}
