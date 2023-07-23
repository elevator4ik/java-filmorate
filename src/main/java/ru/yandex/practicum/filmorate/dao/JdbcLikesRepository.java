package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.LikesRepository;
import ru.yandex.practicum.filmorate.model.mappers.LikesRowMapper;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class JdbcLikesRepository implements LikesRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addLike(int filmId, int userId) {

        final String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) " +
                "VALUES (:filmId, :userId)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("filmId", filmId);
        map.addValue("userId", userId);
        jdbcOperations.update(sqlQuery, map);
    }

    @Override
    public List<Integer> getFilmLikes(int filmId) {//тут берем все user_id тех, кто лайкал
        final String sqlQuery = "SELECT USER_ID " +
                "FROM LIKES " +
                "WHERE FILM_ID = :filmId";
        return jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new LikesRowMapper());
    }

    @Override
    public int getCountFilmLikes(int filmId) {//тут берем просто количество лайков
        final String sqlQuery = "SELECT COUNT(LIKE_ID) AS USER_ID " +//чтобы маппер проглотил
                "FROM LIKES " +
                "WHERE FILM_ID = :filmId";
        List<Integer> list = jdbcOperations.query(sqlQuery, Map.of("filmId", filmId), new LikesRowMapper());

        if (list.size() != 0) {
            return list.get(0);
        } else {
            return 0;
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        final String sqlQuery = "DELETE FROM LIKES " +
                "WHERE FILM_ID = :filmId " +
                "AND USER_ID = :userId";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("filmId", filmId);
        jdbcOperations.update(sqlQuery, map);
    }
}
