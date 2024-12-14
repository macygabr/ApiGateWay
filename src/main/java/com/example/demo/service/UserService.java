package com.example.demo.service;

import com.example.demo.models.exception.NotFoundException;
import com.example.demo.models.user.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    /**
     * Сохранение пользователя
     *
     * @param user пользователь для сохранения
     * @return сохраненный пользователь
     */
    public User save(User user) {
        try {
            return repository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении пользователя: " + e.getMessage(), e);
        }
    }

    /**
     * Создание пользователя
     *
     * @param user пользователь для создания
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @param username имя пользователя
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с именем " + username + " не найден"));
    }

    /**
     * Загрузка пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return реализация UserDetailsService
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        try {
            var username = SecurityContextHolder.getContext().getAuthentication().getName();
            return getByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
    }

    /**
     * Удаление пользователя по email
     *
     */
    public void deleteUser(String username) {
        repository.delete(getByUsername(username));
    }
}