package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController

public class FilmController {

    private FilmStorage storage;

    private FilmService service;

    public FilmController(FilmStorage storage) {
        this.storage = storage;
        this.service = new FilmService(storage);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return storage.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return storage.getFilm(id);
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        return storage.add(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return storage.update(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id,
                        @PathVariable int userId) {
        service.filmLikesManipulating(id, userId, "add");
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id,
                           @PathVariable int userId) {
        service.filmLikesManipulating(id, userId, "delete");
    }

    @GetMapping("/films/popular")
    public List<Film> getTenPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return service.filmLikesManipulating(1, count, "getPopularFilms");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNBadRequestException(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleErrorException(final ErrorException e) {
        return Map.of("error", e.getMessage());
    }
}
