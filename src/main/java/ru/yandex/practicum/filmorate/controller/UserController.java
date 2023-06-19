package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class UserController {
    int id = 1;
    Map<Integer, User> list = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Get all users {}", list.size());
        return new ArrayList<>(list.values());
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) { //юнит-тесты не покрывают то, что проверяется через @Valid
        user.setId(id);
        checkUserData(user);
        id++;
        list.put(user.getId(), user);
        log.info("Put user with id {}", user.getId());
        return list.get(user.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        if (list.containsKey(user.getId())) {
            checkUserData(user);
            list.put(user.getId(), user);
            log.info("Update user with id {}", user.getId());
            return list.get(user.getId());
        } else {
            throw new ValidationException("в базе нет пользователя с таким id");
        }
    }

    private void checkUserData(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения указа некорректно");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
