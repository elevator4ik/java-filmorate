package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.ServiceManipulation.*;


@RestController

public class FilmController {
    private FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return service.getFilmById(id);
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        return service.add(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return service.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id,
                        @PathVariable int userId) {
        service.filmLikesManipulating(id, userId, ADD);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id,
                           @PathVariable int userId) {
        service.filmLikesManipulating(id, userId, DELETE);
    }

    @GetMapping("/films/popular")
    public List<Film> getTenPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return service.getPopularFilms(count);
    }
}
