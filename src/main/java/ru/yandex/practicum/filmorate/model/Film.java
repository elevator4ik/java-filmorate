package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class Film {

    private int id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NotNull
    private int duration;
    private List<Genre> genres;
    private Mpa mpa;
    private int likes;

    public Film(int filmId, String filmName, String filmDescription, LocalDate releaseDate,
                int duration, int likeCount) {
        this.id = filmId;
        this.name = filmName;
        this.description = filmDescription;
        this.releaseDate = releaseDate;
        this.duration = duration;

        this.likes = likeCount;
    }
}

