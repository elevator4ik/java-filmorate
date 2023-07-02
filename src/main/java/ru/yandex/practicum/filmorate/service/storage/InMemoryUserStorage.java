package ru.yandex.practicum.filmorate.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1; //не задаем в интерфейсе id, т.к. он должен быть изменяемым

    @Override
    public List<User> getUsers() {
        log.info("Get all users {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {

        if (users.containsKey(id)) {
            log.info("Get user with id {}", id);
            return users.get(id);
        } else {
            log.info("There is no user with id {}", id);
            throw new NotFoundException("в базе нет пользователя с таким id");
        }
    }

    @Override
    public User add(User user) {
        user.setId(id);
        checkUserData(user);
        id++;
        users.put(user.getId(), user);
        log.info("Put user with id {}", user.getId());
        return users.get(user.getId()); //цепляем из хранилища, чтобы сразу подтвердить корректную запись
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            checkUserData(user);
            users.put(user.getId(), user);
            log.info("Update user with id {}", user.getId());
            return users.get(user.getId());
        } else {
            log.info("There is no user with id {}", user.getId());
            throw new NotFoundException("в базе нет пользователя с таким id");
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
