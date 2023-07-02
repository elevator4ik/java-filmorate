package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;


import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FilmValidationTests {
    FilmController filmController;

    @BeforeEach
    void creating() {
        filmController = new FilmController(new InMemoryFilmStorage());
    }

    @Test
    void creatingFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing",
                LocalDate.of(1946, Month.AUGUST, 20), 100);

        filmController.add(film);

        assertEquals("[Film(id=1, name=nisi eiusmod, description=adipisicing, " +
                "releaseDate=1946-08-20, duration=100, likes=null)]", filmController.getFilms().toString(),
                "Неверное сохранение на сервер.");
    }

    @Test
    void creatingFilmFailReleaseDate() {
        Film film = new Film("nisi eiusmod", "adipisicing",
                LocalDate.of(1880, Month.AUGUST, 20), 100);

        try {
            filmController.add(film);
        } catch (ValidationException e) {
            assertEquals("дата релиза — не раньше 28 декабря 1895 года",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }

    @Test
    void creatingFilmFailDescription() {
        Film film = new Film("nisi eiusmod", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город" +
                " Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20" +
                " миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
                LocalDate.of(1946, Month.AUGUST, 20), 100);

        try {
            filmController.add(film);
        } catch (ValidationException e) {
            assertEquals("максимальная длина описания — 200 символов",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }

    @Test
    void creatingFilmFailDuration() {
        Film film = new Film("nisi eiusmod", "adipisicing",
                LocalDate.of(1980, Month.AUGUST, 20), -100);

        try {
            filmController.add(film);
        } catch (ValidationException e) {
            assertEquals("продолжительность фильма должна быть положительной",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }

    @Test
    void updatingFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing",
                LocalDate.of(1946, Month.AUGUST, 20), 100);

        filmController.add(film);

        Film film2 = new Film("nisiki eiusmod", "adipisicing wrong",
                LocalDate.of(1996, Month.SEPTEMBER, 20), 1100);
        film2.setId(1);
        filmController.update(film2);

        assertEquals("[Film(id=1, name=nisiki eiusmod, description=adipisicing wrong," +
                        " releaseDate=1996-09-20, duration=1100, likes=null)]", filmController.getFilms().toString(),
                "Неверное сохранение на сервер.");
    }

    @Test
    void updatingFilmFailUnknown() {
        Film film = new Film("nisi eiusmod", "adipisicing",
                LocalDate.of(1946, Month.AUGUST, 20), 100);

        filmController.add(film);

        Film film2 = new Film("nisiki eiusmod", "adipisicing wrong",
                LocalDate.of(1996, Month.SEPTEMBER, 20), 1100);
        film2.setId(999);

        try {
            filmController.update(film2);
        } catch (NotFoundException e) {
            assertEquals("в базе нет фильма с таким id",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }
}