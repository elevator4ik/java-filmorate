package ru.yandex.practicum.filmorate.exception;

public class ErrorException extends RuntimeException {

    public ErrorException(String massage) {
        super(massage);
    }
}
