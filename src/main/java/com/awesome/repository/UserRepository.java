package com.awesome.repository;

import com.awesome.controller.dto.UserInsertDTO;
import com.awesome.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumberAndIsEnabled(String phoneNumber, boolean isEnabled);

    Optional<User> findByEmailAndIsEnabled(String phoneNumber, boolean isEnabled);

    void save(UserInsertDTO user);
}
