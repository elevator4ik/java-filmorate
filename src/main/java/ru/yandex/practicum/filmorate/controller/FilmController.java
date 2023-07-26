package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService service;

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
        service.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id,
                           @PathVariable int userId) {
        service.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTenPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return service.getPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return service.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return service.getGenreByID(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpas() {
        return service.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return service.getMpaById(id);
    }
}
