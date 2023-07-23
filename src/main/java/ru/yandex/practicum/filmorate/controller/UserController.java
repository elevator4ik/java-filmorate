package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return service.getUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return service.getUserFriends(id);
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {
        return service.add(user);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        return service.update(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id,
                                       @PathVariable int otherId) {
        return service.mutualFriends(id, otherId);
    }
}
