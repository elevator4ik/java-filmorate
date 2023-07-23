package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class FilmByLikeComparator implements Comparator<Film> {
    @Override
    public int compare(Film f1, Film f2) {
        int i = Integer.compare(f1.getLikes(), f2.getLikes());
        if (i == 0) { //если результат сравнения равен — запись в сет не происходит,значит сортируем по id
            return Integer.compare(f1.getId(), f2.getId());
        } else {
            return -i;
        }
    }
}