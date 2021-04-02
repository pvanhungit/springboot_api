package com.awesome.controller;

import com.awesome.service.auth.SingleSignOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SingleSignOnService singleSignOnService;

    @GetMapping("/token")
    public ResponseEntity getAccessToken() {
        String accessToken = singleSignOnService.retrieveToken();
        return ResponseEntity.ok(String.format("Bearer %s", accessToken));
    }

    @RequestMapping(value = "/root", method = RequestMethod.GET)
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Hello root user");
    }

    @RequestMapping(value = "/member", method = RequestMethod.GET)
    public ResponseEntity<String> getMember() {
        return ResponseEntity.ok("Hello member user");
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Hello Admin user");
    }

    @RequestMapping(value = "/all-user", method = RequestMethod.GET)
    public ResponseEntity<String> getAllUser() {
        return ResponseEntity.ok("Hello All User");
    }

}
