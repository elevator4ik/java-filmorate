package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MPACategoryRepository {

    void addMpaCategory(Mpa newMpa);

    Mpa getMpaCategory(int filmId);

    List<Mpa> getMpaCategories();
}
