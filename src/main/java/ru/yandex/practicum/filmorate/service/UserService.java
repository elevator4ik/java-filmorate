package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUserFriends(int id) {
        List<User> usersFromList = new ArrayList<>();
        List<Integer> friendList = new ArrayList<>(storage.getUser(id).getFriendList());

        for (int i : friendList) {
            usersFromList.add(storage.getUser(i));
        }
        log.info("Get friends of user {} ", id);
        return usersFromList;
    }

    public List<User> friendManipulating(int id, int friendId, String manipulation) {

        checkId(id, friendId);

        User user = storage.getUser(id);
        User friend = storage.getUser(friendId);
        Set<Integer> userFriends = user.getFriendList();
        Set<Integer> friendFriends = friend.getFriendList();
        List<User> mutualFriends = new ArrayList<>();

        if (userFriends == null) {
            userFriends = new HashSet<>();
        }
        if (friendFriends == null) {
            friendFriends = new HashSet<>();
        }
        switch (manipulation) {
            case "add":
                userFriends.add(friendId);
                friendFriends.add(id);
                user.setFriendList(userFriends);
                friend.setFriendList(friendFriends);
                log.info("Add friend with id " + friendId + " to user with id " + id);
                break;
            case "delete":
                userFriends.remove(friendId);
                friendFriends.remove(id);
                user.setFriendList(userFriends);
                friend.setFriendList(friendFriends);
                log.info("Friend with id " + friendId + " was deleted from friends of user with id " + id);
                break;
            case "mutual":
                for (int i : userFriends) {
                    if (friendFriends.contains(i)) {
                        mutualFriends.add(storage.getUser(i));
                    }
                }
                log.info("Get mutual friends of user with id " + id + " and " + friendId);
                break;
        }
        return mutualFriends;
    }

    private void checkId(int id, int friendId) {
        if (id < 1 && friendId < 1) {
            log.warn("Один из переданных id некорректный");
            throw new NotFoundException("Один из переданных id некорректный");
        }
        if (storage.getUser(id) == null && storage.getUser(friendId) == null) {
            log.warn("Одного из переданных id нет в базе");
            throw new ErrorException("Одного из переданных id нет в базе");
        }
    }
}
