package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ServiceManipulation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
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

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUserById(int id) {
        checkId(id);

        return storage.getUser(id);
    }

    public User add(User user) {
        checkUserData(user);

        return storage.add(user);
    }

    public User update(User user) {
        checkId(user.getId());
        checkUserData(user);

        return storage.update(user);
    }


    public List<User> getUserFriends(int id) {
        List<User> usersFromList = new ArrayList<>();

        for (int i : storage.getUser(id).getFriendList()) {
            usersFromList.add(storage.getUser(i));
        }
        log.info("Get friends of user {} ", id);
        return usersFromList;
    }

    public List<User> friendManipulating(int id, int friendId, ServiceManipulation manipulation) {

        checkId(id);
        checkId(friendId);

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
            case ADD:
                userFriends.add(friendId);
                friendFriends.add(id);
                user.setFriendList(userFriends);
                friend.setFriendList(friendFriends);
                log.info("Add friend with id " + friendId + " to user with id " + id);
                break;
            case DELETE:
                userFriends.remove(friendId);
                friendFriends.remove(id);
                user.setFriendList(userFriends);
                friend.setFriendList(friendFriends);
                log.info("Friend with id " + friendId + " was deleted from friends of user with id " + id);
                break;
            case MUTUAL:
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

    private void checkId(int id) {
        if (storage.getUser(id) == null || id < 1) {
            log.warn("Переданный id {} не корректный", id);
            throw new NotFoundException("Переданный id " + id + " не корректный");
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
            log.info("Update user name with id {}", user.getId());
        }
    }
}
