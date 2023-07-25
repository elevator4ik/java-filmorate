package ru.yandex.practicum.filmorate.dao.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsRepository {

    List<Integer> getUserFriends(int userId);

    void setFriends(int userId, int friend);

    void deleteFriends(int userId, int friendId);

    List<User> mutualFriends(int id, int friendId);
}
