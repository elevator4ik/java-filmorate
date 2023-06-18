package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NonNull
    @NotBlank
    private String login;
    @NonNull
    @NotBlank
    @Email
    private String email;
    @NonNull
    private LocalDate birthday;
    private String name;
}
