package com.awesome.service.auth;

import com.awesome.controller.dto.UserCreateDTO;
import com.awesome.controller.dto.UserInsertDTO;

public interface SingleSignOnService {

    String retrieveToken();

    String getUsers();

    UserInsertDTO registerUser(UserCreateDTO userCreateDTO) throws Exception;

    void handleInsertUserFailed(String userKeycloakId);
}
