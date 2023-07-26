package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.interfaces.FriendsRepository;
import ru.yandex.practicum.filmorate.dao.interfaces.UserRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    final FriendsRepository friendsRepository;

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public User getUserById(int id) {
        return userRepository.getUser(id);
    }

    public User add(User user) {
        checkUserData(user);
        return userRepository.add(user);
    }

    public User update(User user) {
        checkUserData(user);
        return userRepository.update(user);
    }

    public List<User> getUserFriends(int id) {

        List<User> friends = new ArrayList<>();
        List<Integer> friendsInt = friendsRepository.getUserFriends(id);
        for (int i : friendsInt) {
            friends.add(getUserById(i));
        }
        log.info("Get friends of user {} ", id);
        return friends;
    }

    public void addFriend(int id, int friendId) {

        if (userRepository.getUser(id) != null & userRepository.getUser(friendId) != null) {
            friendsRepository.setFriends(id, friendId);
            log.info("Add friend with id {} to user with id {}", friendId, id);
        }
    }

    public void deleteFriend(int id, int friendId) {

        friendsRepository.deleteFriends(id, friendId);
        log.info("User with id {} no more hobnob with user with id {}", friendId, id);

    }

    public List<User> mutualFriends(int id, int friendId) {

        List<User> mutualFriends = friendsRepository.mutualFriends(id, friendId);
        System.out.println("\n" + mutualFriends + "\n");
        if (mutualFriends == null) { // постман тесты не принимают null, им нужен пустой список
            mutualFriends = new ArrayList<>();
        }

        log.info("Get mutual friends of user with id {} and {}", id, friendId);

        return mutualFriends;
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
