package com.mireaKR.coworking.service.interfac;

import com.mireaKR.coworking.dto.LoginRequest;
import com.mireaKR.coworking.dto.Response;
import com.mireaKR.coworking.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}