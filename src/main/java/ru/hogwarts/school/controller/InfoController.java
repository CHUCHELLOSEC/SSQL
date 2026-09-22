package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    /**
     * Значение server.port подставляется Spring'ом из активного профиля.
     * Если активен профиль dev — сюда попадёт 8081,
     * если prod — 8082,
     * если профиль не указан — 8080.
     */
    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }
}