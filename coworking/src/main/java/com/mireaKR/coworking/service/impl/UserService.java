package com.mireaKR.coworking.service.impl;

import com.mireaKR.coworking.dto.LoginRequest;
import com.mireaKR.coworking.dto.Response;
import com.mireaKR.coworking.dto.UserDTO;
import com.mireaKR.coworking.entity.User;
import com.mireaKR.coworking.exception.OurException;
import com.mireaKR.coworking.repo.UserRepository;
import com.mireaKR.coworking.service.interfac.IUserService;
import com.mireaKR.coworking.utils.JWTUtils;
import com.mireaKR.coworking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response = new Response();
        try {
            // Если роль не указана, то назначаем "USER" по умолчанию
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            // Проверяем, существует ли уже пользователь с таким email
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " Уже существует");
            }
            // Шифруем пароль
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Сохраняем пользователя
            User savedUser = userRepository.save(user);
            // Преобразуем в DTO
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Произошла ошибка при регистрации: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            // Аутентификация
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));

            // Поиск пользователя в базе данных
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("Пользователь не найден"));

            // Генерация JWT токена
            var token = jwtUtils.generateToken((UserDetails) user);

            // Вычисление времени истечения токена (через 7 дней)
            LocalDateTime expirationDate = LocalDateTime.now().plusDays(7);

            // Заполнение ответа
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime(expirationDate); // Устанавливаем срок действия токена как LocalDateTime
            response.setMessage("Успешный вход");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка при входе пользователя: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Успешно");
            response.setUserList(userDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка при получении всех пользователей: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Пользователь не найден"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Успешно");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка при получении истории бронирований пользователей: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try {
            userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Пользователь не найден"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Пользователь успешно удален");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка удаления пользователя: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Пользователь не найден"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Успешно");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка при получении пользователя: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("Пользователь не найден"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("Успешно");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ошибка получения информации о пользователе: " + e.getMessage());
        }
        return response;
    }
}
