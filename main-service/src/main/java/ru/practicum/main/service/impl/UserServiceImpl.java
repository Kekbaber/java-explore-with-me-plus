package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.NewUserRequest;
import ru.practicum.main.dto.request.UsersRequest;
import ru.practicum.main.dto.response.UserDto;
import ru.practicum.main.exception.user.EmailAlreadyExistsException;
import ru.practicum.main.exception.user.UserNotFoundException;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service  // Добавлено
@RequiredArgsConstructor  // Добавлено для инъекции через конструктор
@Transactional  // Добавлено на уровень класса
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;  // Добавлен final

    @Override
    public UserDto createUser(NewUserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Пользователь с email '" + userRequest.getEmail() + "' уже существует"
            );
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsers(UsersRequest request) {
        List<User> users;

        // Получаем пользователей с фильтром или всех
        if (request.getIds() != null && !request.getIds().isEmpty()) {
            users = userRepository.findByIds(request.getIds());
        } else {
            users = userRepository.findAllOrdered();
        }


        int from = request.getFrom() != null ? request.getFrom() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;


        if (from >= users.size()) {
            return List.of();
        }

        int to = Math.min(from + size, users.size());

        return users.subList(from, to).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id: " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}