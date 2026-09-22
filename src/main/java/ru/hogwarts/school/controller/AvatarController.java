package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    // Эндпоинт с пагинацией
    // GET http://localhost:8080/avatars?page=0&size=5
    @GetMapping
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam int page,
            @RequestParam int size) {
        return ResponseEntity.ok(avatarService.getAllAvatars(page, size));
    }
}