package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Film;


public interface MpaFilmRepository {

    void addMpaToFilm(Film film);

    void deleteMpaFromFilm(Film film);

    Integer getFilmMpa(int filmId);

}
