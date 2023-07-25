package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.FriendsRepository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.mappers.FriendIdRowMapper;
import ru.yandex.practicum.filmorate.model.mappers.UserRowMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcFriendsRepository implements FriendsRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Integer> getUserFriends(int userId) {
        final String sqlQuery = "SELECT FRIEND_ID " +
                "FROM FRIENDS " +
                "WHERE USER_ID = :userId";

        return jdbcOperations.query(sqlQuery, Map.of("userId", userId), new FriendIdRowMapper());
    }

    @Override
    public void setFriends(int userId, int friendId) {
        final String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) " +
                "VALUES (:userId, " +
                ":friendId); ";


        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public void deleteFriends(int userId, int friendId) {
        final String sqlQuery = "DELETE FROM FRIENDS " +
                "WHERE USER_ID = :userId " +
                "AND FRIEND_ID = :friendId";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public List<User> mutualFriends(int userId, int friendId) {

        final String sqlQuery2 = "SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID IN (SELECT FR.FRIEND_ID " +
                "FROM (SELECT USER_ID, FRIEND_ID " +
                "FROM FRIENDS " +
                "WHERE USER_ID = :userId) AS FR " +
                "JOIN (SELECT FRIEND_ID " +
                "FROM FRIENDS " +
                "WHERE USER_ID = :friendId) AS F ON FR.FRIEND_ID = F.FRIEND_ID)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("friendId", friendId);
        return jdbcOperations.query(sqlQuery2, map, new UserRowMapper());
    }
}
