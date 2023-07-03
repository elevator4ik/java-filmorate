package ru.yandex.practicum.filmorate.model;

import java.util.Comparator;

public class FilmByLikeComparator implements Comparator<Film> {
    @Override
    public int compare(Film f1, Film f2) {
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
        if (s1 == s2) { //если результат сравнения равен — запись в сет не происходит,
            //поэтому добавил дополнительную сортировку по id
            s1 = f1.getId();
            s2 = f2.getId();
        }
        return Integer.compare(s1, s2);
    }
}