package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    User getUser(int id);

    User add(User user);

    User update(User user);
}