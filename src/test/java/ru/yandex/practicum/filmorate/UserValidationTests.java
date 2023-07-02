package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;


import java.time.LocalDate;
import java.time.Month;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserValidationTests {
    UserController userController;

    @BeforeEach
    void creating() {
        userController = new UserController(new InMemoryUserStorage());
    }

    @Test
    void creatingUser() {
        User user = new User("dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        user.setName("Nick Name");
        userController.add(user);
        assertEquals("[User(id=1, login=dolore, email=mail@mail.ru, birthday=1946-08-20, name=Nick Name," +
                " friendList=null)]", userController.getUsers().toString(), "Неверное сохранение на сервер.");

    }

    @Test
    void creatingUserFailLogin() {
        User user = new User("dolore asd", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        try {
            userController.add(user);
        } catch (ValidationException e) {
            assertEquals("логин не может быть пустым и содержать пробелы",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }

    @Test
    void creatingUserFailBirthday() {
        User user = new User("doloreasd", "mail@mail.ru", LocalDate.of(2946, Month.AUGUST, 20));
        try {
            userController.add(user);
        } catch (ValidationException e) {
            assertEquals("дата рождения указа некорректно",
                    e.getMessage(), "Неверное сохранение на сервер.");
        }
    }

    @Test
    void creatingUserWithEmptyName() {
        User user = new User("dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        userController.add(user);
        assertEquals("[User(id=1, login=dolore, email=mail@mail.ru, birthday=1946-08-20, name=dolore, " +
                "friendList=null)]", userController.getUsers().toString(), "Неверное сохранение на сервер.");

    }

    @Test
    void updatingUser() {
        User user = new User("dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        user.setName("Nick Name");
        userController.add(user);
        User user2 = new User("doloreUpdate", "mail@yandex.ru",
                LocalDate.of(1976, Month.SEPTEMBER, 20));
        user2.setName("est adipisicing");
        user2.setId(1);
        userController.update(user2);
        assertEquals("[User(id=1, login=doloreUpdate, email=mail@yandex.ru, birthday=1976-09-20, " +
                        "name=est adipisicing, friendList=null)]", userController.getUsers().toString(),
                "Неверное сохранение на сервер.");
    }

    @Test
    void updatingUserFailUnknown() {
        User user = new User("dolore", "mail@mail.ru", LocalDate.of(1946, Month.AUGUST, 20));
        user.setName("Nick Name");

        userController.add(user);

        User user2 = new User("doloreUpdate", "mail@yandex.ru",
                LocalDate.of(1976, Month.SEPTEMBER, 20));
        user2.setName("est adipisicing");
        user.setId(9999);
        try {
            userController.update(user2);
        } catch (NotFoundException e) {
            assertEquals("в базе нет пользователя с таким id", e.getMessage(),
                    "Неверное сохранение на сервер.");
        }
    }
}
