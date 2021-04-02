package com.awesome.controller;

import com.awesome.controller.dto.UserCreateDTO;
import com.awesome.domain.User;
import com.awesome.exception.AppException;
import com.awesome.service.auth.SingleSignOnService;
import com.awesome.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SingleSignOnService singleSignOnService;

    @GetMapping("")
    public ResponseEntity<String> findAllUsers(){
        return ResponseEntity.ok(singleSignOnService.getUsers());
    }

    @GetMapping("/details")
    public ResponseEntity<User> findUserByPhoneOrEmail(@RequestParam(value = "phone",defaultValue = "") String phone,
                                                    @RequestParam(value = "email", defaultValue = "") String email){
        Optional<User> user;

        if(!phone.isBlank()) user = userService.findByPhoneNumber(phone);
        else user = userService.findByEmail(email);

        if(user.isPresent())
            return ResponseEntity.ok(user.get());

        throw new AppException(HttpStatus.NOT_FOUND, "EX10002", Arrays.asList(!phone.isBlank() ? phone : email));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        Optional<User> user = userService.findById(id);

        if(user.isEmpty()) {
            throw new AppException(HttpStatus.NOT_FOUND, "EX10002", Arrays.asList(String.format("userID: %s", id)));
        }
        return ResponseEntity.ok(user.get());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createMemberUser(@Validated @RequestBody UserCreateDTO userCreateDTO) throws Exception {

        //register keycloak user
        var userInsertDTO = singleSignOnService.registerUser(userCreateDTO);

        //insert user to database.
        User userInserted = userService.createUser(userInsertDTO);

        //handle insert failed in previous step.
        if (userInserted == null || (userInserted != null && userInserted.getUserId() == null)) {
            singleSignOnService.handleInsertUserFailed(userInserted.getUserKeyCloakId());
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "EX60004", Arrays.asList("Insert user failed!"));
        }
        return ResponseEntity.ok("User created!");
    }

    @DeleteMapping(value = "/details/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id){
        Optional<User> user = userService.findById(id);

        if ( user.isEmpty() ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),HttpStatus.BAD_REQUEST);
        }

        userService.deleteUser(user.get());
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }
}