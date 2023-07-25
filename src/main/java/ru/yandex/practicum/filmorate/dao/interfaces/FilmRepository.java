package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    List<Film> getFilms();

    Film getFilm(int id);

    Film add(Film film);

    Film update(Film film);

    List<Film> getFilmsByLikes(int count);
}