package ru.yandex.practicum.filmorate.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j

public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;

    @Override
    public List<Film> getFilms() {
        log.info("Get all films {}", films.size());
        return new ArrayList<>(films.values());

    }

    @Override
    public Film getFilm(int id) {

        if (films.containsKey(id)) {
            log.info("Get user with id {}", id);
            return films.get(id);
        } else {
            log.info("There is no film with id {}", id);
            throw new NotFoundException("в базе нет фильма с таким id");
        }
    }

    @Override
    public Film add(Film film) {
        film.setId(id);
        checkFilmData(film);
        id++;
        films.put(film.getId(), film);
        log.info("Put film with id {}", film.getId());
        return films.get(film.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            checkFilmData(film);
            films.put(film.getId(), film);
            log.info("Update film with id {}", film.getId());
            return films.get(film.getId());
        } else {
            throw new NotFoundException("в базе нет фильма с таким id");
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
