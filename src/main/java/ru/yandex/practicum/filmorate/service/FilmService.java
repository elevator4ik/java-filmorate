package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmByLikeComparator;
import ru.yandex.practicum.filmorate.model.ServiceManipulation;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.model.ServiceManipulation.*;
import static ru.yandex.practicum.filmorate.storage.FilmStorage.FIRST_FILM;


@Service
@Slf4j
public class FilmService {

    FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film getFilmById(int id) {
        checkFilmId(id);

        return storage.getFilm(id);
    }

    public Film add(Film film) {
        checkFilmData(film);

        return storage.add(film);
    }

    public Film update(Film film) {
        checkFilmId(film.getId());
        checkFilmData(film);

        return storage.update(film);
    }

    public void filmLikesManipulating(int filmId, int userId, ServiceManipulation manipulation) {
        checkFilmId(filmId);
        checkId(userId);

        Film film = storage.getFilm(filmId);
        Set<Integer> likes = film.getLikes();

        if (likes == null) {
            likes = new HashSet<>();
        }
        if (manipulation.equals(ADD)) {
            if (likes.add(userId)) {
                film.setLikes(likes);
                log.info("Пользователь с id " + userId + " поставил свой царский лайк фильму c id " + filmId);
            } else {
                log.warn("Пользователь с id " + userId + " уже лайкал фильм c id " + filmId);
                throw new ErrorException("Пользователь с id " + userId + " уже лайкал фильм c id " + filmId);
            }
        } else if (manipulation.equals(DELETE)) {
            if (likes.remove(userId)) {
                film.setLikes(likes);
                log.info("Пользователь с id " + userId + " удалил свой царский лайк у фильма c id " + filmId);
            } else {
                log.warn("Пользователь с id " + userId + " не лайкал фильм c id " + filmId);
                throw new ErrorException("Пользователь с id " + userId + " не лайкал фильм c id " + filmId);
            }
        }
    }

    public List<Film> getPopularFilms(int count) {

        List<Film> popularFilms = new ArrayList<>(storage.getFilms());

        popularFilms.sort(new FilmByLikeComparator());
        log.info("Список популярных фильмов составлен");
        if (popularFilms.size() <= count) {
            return popularFilms;
        } else {
            return popularFilms.subList(0, count);
        }
    }

    private void checkFilmId(int id) {
        checkId(id);
        if (storage.getFilm(id) == null) {
            log.warn("Переданного id нет в базе");
            throw new NotFoundException("Переданного id нет в базе");
        }
    }

    private void checkId(int id) {
        if (id < 1) {
            log.warn("Переданный id фильма или пользователя некорректный");
            throw new NotFoundException("Переданный id фильма или пользователя некорректный");
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