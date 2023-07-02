package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmStorage {

    LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    List<Film> getFilms();

    Film getFilm(int id);

    Film add(Film film);

    Film update(Film film);

}
