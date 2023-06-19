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

    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    int id = 1;
    Map<Integer, Film> list = new HashMap<>();

    @GetMapping("/films")
    public Map<Integer, Film> getFilms() {
        log.info("Get all films {}", list.size());
        return list;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) { //юнит-тесты не покрывают то, что проверяется через @Valid
        film.setId(id);
        checkFilmData(film);
        id++;
        list.put(film.getId(), film);
        log.info("Put film with id {}", film.getId());
        return list.get(film.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        if (list.containsKey(film.getId())) {
            checkFilmData(film);
            list.put(film.getId(), film);
            log.info("Update film with id {}", film.getId());
            return list.get(film.getId());
        } else {
            throw new ValidationException("в базе нет фильма с таким id");
        }
    }

    private void checkFilmData(Film film) {

        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
