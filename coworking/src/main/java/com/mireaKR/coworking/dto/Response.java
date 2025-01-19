package com.mireaKR.coworking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;  // Код состояния ответа (например, 200, 404, 500 и т.д.)
    private String message;  // Сообщение, поясняющее результат

    private String token;  // Токен (например, для аутентификации)
    private String role;   // Роль пользователя
    private LocalDateTime expirationTime;  // Время истечения токена (если применимо)

    private String bookingConfirmationCode;  // Код подтверждения бронирования (если применимо)

    private UserDTO user;  // Информация о пользователе (если возвращается)
    private RoomDTO room;  // Информация о комнате (если возвращается)
    private BookingDTO booking;  // Информация о бронировании (если возвращается)

    private List<UserDTO> userList;  // Список пользователей (например, для получения всех пользователей)
    private List<RoomDTO> roomList;  // Список комнат (например, для получения всех комнат)
    private List<BookingDTO> bookingList;  // Список бронирований (например, для получения всех бронирований)

    // Опционально: можно добавить конструкторы для удобства работы с Response
}
