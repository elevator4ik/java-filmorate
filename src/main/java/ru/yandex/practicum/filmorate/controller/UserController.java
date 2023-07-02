package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class UserController {

    UserStorage storage;
    UserService service;

    public UserController(UserStorage storage) {
        this.storage = storage;
        this.service = new UserService(storage);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return storage.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return service.getUserFriends(id);
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {
        return storage.add(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        service.friendManipulating(id, friendId, "add");
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        service.friendManipulating(id, friendId, "delete");
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id,
                                       @PathVariable int otherId) {
       return service.friendManipulating(id, otherId, "mutual");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNBadRequestException(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleErrorException(final ErrorException e) {
        return Map.of("error", e.getMessage());
    }
}
