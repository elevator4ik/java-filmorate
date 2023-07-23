package ru.yandex.practicum.filmorate.dao.interfaces;

import java.util.List;

public interface LikesRepository {
    void addLike(int filmId, int userId);

    List<Integer> getFilmLikes(int filmId);

    int getCountFilmLikes(int filmId);

    void deleteLike(int filmId, int userId);
}
