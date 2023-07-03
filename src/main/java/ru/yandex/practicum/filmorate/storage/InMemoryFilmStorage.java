package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j

public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        log.info("Get all films {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        checkId(id);
        log.info("Get user with id {}", id);
        return films.get(id);
    }

    @Override
    public Film add(Film film) {
        film.setId(id);
        id++;
        films.put(film.getId(), film);
        log.info("Put film with id {}", film.getId());
        return films.get(film.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @Override
    public Film update(Film film) {
        checkId(film.getId());
        films.put(film.getId(), film);
        log.info("Update film with id {}", film.getId());
        return films.get(film.getId());
    }

    private void checkId(int id) {
        if (films.get(id) == null || id < 1) {
            log.warn("Переданный id {} не корректный", id);
            throw new NotFoundException("Переданный id " + id + " не корректный");
        }
    }
}
