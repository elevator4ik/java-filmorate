package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {


    void addGenre(Genre genre);

    Genre getGenre(int i);

    List<Genre> getGenres();

    List<Genre> findByIds(List<Integer> ids);
}