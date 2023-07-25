package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interfaces.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmByLikeComparator;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.model.Constants.FIRST_FILM;


@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    final MpaFilmRepository mpaFilmRepository;
    final MPACategoryRepository mpaCategoryRepository;
    final FilmGenreRepository filmGenreRepository;
    final GenreRepository genreRepository;
    final LikesRepository likesRepository;
    final FilmRepository filmRepository;


    public List<Genre> getGenres() {
        return genreRepository.getGenres();
    }

    public Genre getGenreByID(int id) {
        return genreRepository.getGenre(id);
    }

    public List<Mpa> getMpas() {
        return mpaCategoryRepository.getMpaCategories();
    }

    public Mpa getMpaById(int id) {
        return mpaCategoryRepository.getMpaCategory(id);
    }

    public List<Film> getFilms() {

        List<Film> films = filmRepository.getFilms();
        getMpaAndGenreInFilms(films);
        return films;
    }

    public Film getFilmById(int id) {

        Film film = filmRepository.getFilm(id);
        film.setMpa(mpaCategoryRepository.getFilmMpaCategory(id));
        film.setGenres(filmGenreRepository.getFilmGenres(id));
        return film;
    }

    public Film add(Film film) {
        checkFilmData(film);
        filmRepository.add(film);
        List<Genre> list = film.getGenres();
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            mpaFilmRepository.addMpaToFilm(film);
        }
        if (list != null && !list.isEmpty()) {
            for (Genre g : list) {
                filmGenreRepository.addGenreToFilm(film.getId(), g.getId());
            }
        }
        return film;
    }

    public Film update(Film film) {
        checkFilmData(film);
        checkUpdate(film.getId());
        filmRepository.update(film);
        updateMpaInFilm(film);
        updateGenreInFilm(film);
        log.info("Фильм с id {} обновлен", film.getId());
        return getFilmById(film.getId());
    }

    public void addLikeToFilm(int filmId, int userId) {
        checkId(userId);

        try {
            likesRepository.addLike(filmId, userId);
            log.info("Пользователь с id {} поставил свой царский лайк фильму c id {}", userId, filmId);
        } catch (SQLException e) {
            log.warn("Пользователь с id {} уже лайкал фильм c id {}", userId, filmId);
            throw new ErrorException("Пользователь с id " + userId + " уже лайкал фильм c id " + filmId);
        }
    }

    public void deleteLikeFromFilm(int filmId, int userId) {
        checkId(userId);

        try {
            likesRepository.deleteLike(filmId, userId);
            log.info("Пользователь с id {} удалил свой царский лайк у фильма c id {}", userId, filmId);
        } catch (SQLException e) {
            log.warn("Пользователь с id {} не лайкал фильм c id {}", userId, filmId);
            throw new ErrorException("Пользователь с id " + userId + " не лайкал фильм c id " + filmId);
        }
    }

    public List<Film> getPopularFilms(int count) {

        List<Film> popularFilms = filmRepository.getFilmsByLikes(count);
        System.out.println("\n" + popularFilms + "\n");

        if (popularFilms != null && !popularFilms.isEmpty()) {
            getMpaAndGenreInFilms(popularFilms);
        } else {
            popularFilms = filmRepository.getFilms();
            popularFilms.sort(new FilmByLikeComparator());
            getMpaAndGenreInFilms(popularFilms);
        }

        log.info("Список популярных фильмов составлен");
        return popularFilms;
    }

    private void getMpaAndGenreInFilms(List<Film> films) {

        Map<Integer, Mpa> list = mpaCategoryRepository.getAllFilmsMpaCategories(); // объем выгружаемых данных ограничен,
        // , поэтому смело можно выгружать все значения
        Map<Integer, List<Genre>> genres = filmGenreRepository.getAllFilmsGenres(); // аналогично
        List<Genre> genresForWrite; //без этого пишется null и постман выкидывает ошибку в тестах
        for (Film f : films) {
            f.setMpa(list.get(f.getId()));
            genresForWrite = genres.get(f.getId());
            if (genresForWrite != null) {
                f.setGenres(genresForWrite);
            } else {
                genresForWrite = new ArrayList<>();
                f.setGenres(genresForWrite);
            }
        }
    }

    private void updateMpaInFilm(Film film) {
        if (film.getMpa() != null) {
            mpaFilmRepository.deleteMpaFromFilm(film);
            mpaFilmRepository.addMpaToFilm(film);
        }
    }

    private void updateGenreInFilm(Film film) {
        List<Genre> genreList = filmGenreRepository.getFilmGenres(film.getId());
        List<Genre> filmGenres = film.getGenres();
        List<Integer> genreListIds = new ArrayList<>();
        List<Integer> filmGenresIds = new ArrayList<>();

        for (Genre g : genreList) {
            genreListIds.add(g.getId());
        }
        if (filmGenres != null) {
            for (Genre g : filmGenres) {
                filmGenresIds.add(g.getId());
            }
        } else {
            deleteGenreList(film, genreList);
        }
        if (!filmGenresIds.isEmpty()) {
            if (genreListIds.isEmpty()) {
                for (int i : filmGenresIds) {
                    filmGenreRepository.addGenreToFilm(film.getId(), i);
                }
            } else {
                for (int i : filmGenresIds) {
                    if (!genreListIds.contains(i)) {
                        filmGenreRepository.addGenreToFilm(film.getId(), i);
                    }
                }
                for (int i : genreListIds) {
                    if (!filmGenresIds.contains(i)) {
                        filmGenreRepository.deleteGenreFromFilm(film.getId(), i);
                    }
                }
            }
        } else {
            deleteGenreList(film, genreList);
        }
    }


    private void deleteGenreList(Film film, List<Genre> genreList) {
        for (Genre g : genreList) {
            filmGenreRepository.deleteGenreFromFilm(film.getId(), g.getId());
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

    private void checkUpdate(int id) {
        checkId(id);
        Film film = null;
        List<Film> films = getFilms();
        for (Film f : films) {
            if (id == f.getId()) {
                film = f;
                break;
            }
        }
        if (film == null) {
            log.warn("Переданного id {} нет в базе", id);
            throw new NotFoundException("Переданного id " + id + " нет в базе");
        }
    }
}