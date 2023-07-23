package ru.yandex.practicum.filmorate.dao.interfaces;


import java.util.List;

public interface FilmGenreRepository {
    void addGenreToFilm(int filmId, int genreId);

    void deleteGenreFromFilm(int filmId, int genreId);

    List<Integer> getFilmGenres(int i);
}
