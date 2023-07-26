package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.mappers.UserRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public User getUser(int id) {
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = :userId ";
        final List<User> users = jdbcOperations.query(sqlQuery, Map.of("userId", id), new UserRowMapper());

        if (users.size() != 1) {
            log.info("Something wrong! Count of users is {}", users.size());
            throw new NotFoundException("There is no user with id " + id);
        }
        log.info("Get user with id {}", id);
        return users.get(0);
    }

    @Override
    public List<User> getUsers() {
        final String sqlQuery = "SELECT * FROM USERS";
        log.info("Get all users.");
        return jdbcOperations.query(sqlQuery, new UserRowMapper());
    }

    @Override
    public User add(User user) {
        final String sqlQuery = "INSERT INTO USERS (EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES (:email, :login, :name, :birthday) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        jdbcOperations.update(sqlQuery, map, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Add user with id {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (getUser(user.getId()) == null) {
            throw new NotFoundException("There is no user with id " + user.getId());
        }
        String sqlQuery = "UPDATE USERS " +
                "SET EMAIL = :email," +
                "USER_LOGIN = :login, " +
                "USER_NAME = :name," +
                "BIRTHDAY = :birthday " +
                "WHERE USER_ID = :user_id";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        map.addValue("user_id", user.getId());
        jdbcOperations.update(sqlQuery, map);
        log.info("Updated user with id {}", user.getId());
        return getUser(user.getId());
    }
}
