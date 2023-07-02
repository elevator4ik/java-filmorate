package ru.yandex.practicum.filmorate.service.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> users = new HashMap<>();

    List<User> getUsers();

    User getUser(int id);

    User add(User user);

    User update(User user);
}
