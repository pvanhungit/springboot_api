package com.awesome.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheck用API
 *
 * APIを各サービス毎に分割した場合は、実行サービスクラスを変更してください。
 */

@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping(value = "/")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("good to go");
    }
}