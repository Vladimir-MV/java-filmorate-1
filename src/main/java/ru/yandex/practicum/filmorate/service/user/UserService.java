package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User add(User user) {
        if (user.getId() != null && userStorage.doesUserExist(user.getId()))
            throw new UserAlreadyExistException(String.format("User with id=%s already exists", user.getId()));
        checkName(user);

        userStorage.add(user);
        log.debug(String.format("new user with id=%s added successfully", user.getId()));

        return user;
    }

    public User update(User user) {
        validate(user.getId());
        checkName(user);

        userStorage.update(user);
        log.debug(String.format("user data with id=%s has been successfully updated", user.getId()));

        return user;
    }
    public User getUserById(int userId) {
        validate(userId);

        Optional<User> optionalUser = userStorage.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id=%s not found", userId));
        }

        return optionalUser.get();
    }

    public void addFriend(int userId, int friendId) {
        validate(userId);
        validate(friendId);

        userStorage.addFriend(userId, friendId);
        log.debug(String.format("The user with id=%s has added the user with id=%s to friends", userId, friendId));
    }

    public void deleteFriend(int userId, int friendId) {
        validateFriendship(userId, friendId);

        userStorage.deleteFriend(userId, friendId);
        log.debug(String.format("The user with id=%s has removed the user with id=%s from friends", userId, friendId));
    }

    public List<User> getAllFriends(int userId) {
        validate(userId);
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        validate(userId);
        validate(otherUserId);
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    private void validate(int userId) {
        if (!userStorage.doesUserExist(userId))
            throw new UserNotFoundException(String.format("User with id=%s not found", userId));
    }

    private void checkName(User user) {    // проверяет -> name == null и пустое ли, и если да присваивает логин
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }

    private void validateFriendship(int userId, int friendId) {
        if (!userStorage.doesFriendExist(userId, friendId))
            throw new FriendNotFoundException(String.format("the user with id=%s does not have a friend with user id=%s",
                    userId, friendId));
    }
}
