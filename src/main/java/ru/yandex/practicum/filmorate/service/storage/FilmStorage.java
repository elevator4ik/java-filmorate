package ru.yandex.practicum.filmorate.service.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    Map<Integer, Film> films = new HashMap<>();

    List<Film> getFilms();

    Film getFilm(int id);

    Film add(Film film);

    Film update(Film film);

}
