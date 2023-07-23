package ru.yandex.practicum.filmorate.dao.interfaces;

import java.util.List;

public interface FriendsRepository {

    List<Integer> getUserFriends(int userId);

    void setFriends(int userId, int friend);

    void deleteFriends(int userId, int friendId);

    List<Integer> mutualFriends(int id, int friendId);
}
