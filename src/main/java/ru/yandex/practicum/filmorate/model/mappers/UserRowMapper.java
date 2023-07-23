package ru.yandex.practicum.filmorate.model.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("USER_LOGIN"),
                rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate(),
                rs.getString("USER_NAME")
        );
    }
}