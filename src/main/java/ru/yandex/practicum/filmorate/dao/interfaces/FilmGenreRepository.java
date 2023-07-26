package ru.yandex.practicum.filmorate.dao.interfaces;


import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface FilmGenreRepository {

    void addGenreToFilm(int filmId, int genreId);

    void deleteGenreFromFilm(int filmId, int genreId);

    List<Genre> getFilmGenres(int i);

    Map<Integer, List<Genre>> getAllFilmsGenres();

    Map<Integer, List<Genre>> getFilmGenresByLikes(int count);
}
