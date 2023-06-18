package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class UserController {
    int id = 1;
    List<User> list = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return list;
    }

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {//юнит-тесты не покрывают то, что проверяется через @Valid
        user.setId(id);
        checkUserData(user);
        id++;
        list.add(user);
        return list.get(list.size() - 1);
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        checkUserData(user);
        User newUser = null;

        for (int i = 0; i < list.size(); i++) {
            if (user.getId() == list.get(i).getId()) {
                list.remove(i);
                list.add(i, user);
                newUser = user;
            }
        }
        if (newUser == null) {
            throw new ValidationException("в базе нет пользователя с таким id");
        } else {
            return newUser;
        }
    }

    private void checkUserData(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения указа некорректно");
        }
        if(user.getName() == null){
            user.setName("common");
        }
    }
}
