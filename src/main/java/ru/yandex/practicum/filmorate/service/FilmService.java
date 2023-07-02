package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.FilmStorage;

import java.util.*;


@Service
@Slf4j
public class FilmService {

    FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> filmLikesManipulating(int filmId, int userIdOrCount, String manipulation) {
        //вместо 3‑х методов 1 со свитчем для читаемости и чтобы не дублировать код.
        checkId(filmId, userIdOrCount);

        Film film = storage.getFilm(filmId);
        Set<Integer> likes = film.getLikes();
        List<Film> popularFilms = new ArrayList<>();

        if (likes == null) {
            likes = new HashSet<>();
        }
        switch (manipulation) {
            case "add":
                if (likes.contains(userIdOrCount)) {
                    log.warn("Пользователь с id " + userIdOrCount + " уже лайкал фильм c id " + filmId);
                    throw new ErrorException("Пользователь с id " + userIdOrCount + " уже лайкал фильм c id " + filmId);
                } else {
                    likes.add(userIdOrCount);
                    film.setLikes(likes);
                    log.info("Пользователь с id " + userIdOrCount + " поставил свой царский лайк фильму c id " + filmId);
                }
                break;
            case "delete":
                if (!likes.contains(userIdOrCount)) {
                    log.warn("Пользователь с id " + userIdOrCount + " не лайкал фильм c id " + filmId);
                    throw new ErrorException("Пользователь с id " + userIdOrCount + " не лайкал фильм c id " + filmId);
                } else {
                    likes.remove(userIdOrCount);
                    film.setLikes(likes);
                    log.info("Пользователь с id " + userIdOrCount + " удалил свой царский лайк у фильма c id " + filmId);
                }
                break;
            case "getPopularFilms":

                Set<Film> set = new TreeSet<>((f1, f2) -> {
                    int s1;
                    int s2;
                    if (f1.getLikes() == null || f1.getLikes().isEmpty()) {
                        s1 = 0;
                    } else {
                        s1 = -(f1.getLikes().size());
                    }
                    if (f2.getLikes() == null || f2.getLikes().isEmpty()) {
                        s2 = 0;
                    } else {
                        s2 = -(f2.getLikes().size());
                    }
                    if (s1 == s2) {//если результат сравнения равен - запись в сет не происходит,
                        //поэтому добавил дополнительную сортировку по id
                        s1 = f1.getId();
                        s2 = f2.getId();
                    }
                    return Integer.compare(s1, s2);
                });
                set.addAll(storage.getFilms());
                for (Film f : set) {
                    if (popularFilms.size() < userIdOrCount) {
                        popularFilms.add(f);
                    } else {
                        log.info("Количество фильмов больше размера count");
                        break;
                    }
                }
                log.info("Список популярных фильмов составлен");
                break;
        }
        return popularFilms;
    }

    private void checkId(int filmId, int userIdOrCount) {
        if (filmId < 1 | userIdOrCount < 1) {
            log.warn("Переданный id фильма или пользователя некорректный");
            throw new NotFoundException("Переданный id фильма или пользователя некорректный");
        }
        if (storage.getFilm(filmId) == null) {
            log.warn("Переданного id нет в базе");
            throw new NotFoundException("Переданного id нет в базе");
        }
    }
}
