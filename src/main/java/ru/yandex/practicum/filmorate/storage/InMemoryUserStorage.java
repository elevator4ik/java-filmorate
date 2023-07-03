package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1; //не задаем в интерфейсе id, т.к. он должен быть изменяемым
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        log.info("Get all users {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        checkId(id);
        log.info("Get user with id {}", id);
        return users.get(id);
    }

    @Override
    public User add(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        log.info("Put user with id {}", user.getId());
        return users.get(user.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @Override
    public User update(User user) {
        checkId(user.getId());
        users.put(user.getId(), user);
        log.info("Update user with id {}", user.getId());
        return users.get(user.getId());
    }

    private void checkId(int id) {
        if (users.get(id) == null || id < 1) {
            log.warn("Переданный id {} не корректный", id);
            throw new NotFoundException("Переданный id " + id + " не корректный");
        }
    }
}
