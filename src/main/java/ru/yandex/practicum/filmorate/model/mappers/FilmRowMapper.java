package ru.yandex.practicum.filmorate.model.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.interfaces.LikesRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    final LikesRepository likesRepository;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                likesRepository.getCountFilmLikes(rs.getInt("FILM_ID"))
        );
    }
}