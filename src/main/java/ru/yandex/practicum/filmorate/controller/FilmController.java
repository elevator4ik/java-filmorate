package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;


@RestController
@Slf4j
public class FilmController {

    int id = 1;
    List<Film> list = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        return list;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) { //юнит-тесты не покрывают то, что проверяется через @Valid
        film.setId(id);
        checkFilmData(film);
        id++;
        list.add(film);
        return list.get(list.size() - 1);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {

        checkFilmData(film);
        Film newFilm = null;

        for (int i = 0; i < list.size(); i++) {
            if (film.getId() == list.get(i).getId()) {
                list.remove(i);
                list.add(i, film);
                newFilm = film;
            }
        }
        if (newFilm == null) {
            throw new ValidationException("в базе нет фильма с таким id");
        } else {
            return newFilm;
        }
    }

    private void checkFilmData(Film film) {

        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
