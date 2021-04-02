package com.awesome.service.user;

import com.awesome.controller.dto.UserInsertDTO;
import com.awesome.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    Optional<User> findById(Long userId);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    User createUser(UserInsertDTO userInsertDTO) throws Exception;

    void deleteUser(User User);

}
