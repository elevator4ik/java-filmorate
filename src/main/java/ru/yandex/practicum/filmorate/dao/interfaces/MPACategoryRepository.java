package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

public interface MPACategoryRepository {

    void addMpaCategory(Mpa newMpa);

    Mpa getMpaCategory(int mpaId);

    Map<Integer, Mpa> getAllFilmsMpaCategories();

    Mpa getFilmMpaCategory(int filmId);

    List<Mpa> getMpaCategories();
}
