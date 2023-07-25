package ru.yandex.practicum.filmorate.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface LikesRepository {
    void addLike(int filmId, int userId) throws SQLException;

    List<Integer> getFilmLikes(int filmId);

    int getCountFilmLikes(int filmId);

    void deleteLike(int filmId, int userId) throws SQLException;
}
