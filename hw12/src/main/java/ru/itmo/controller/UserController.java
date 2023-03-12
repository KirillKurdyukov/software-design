package ru.itmo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.itmo.model.User;
import ru.itmo.repository.UserRepository;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/save/user")
    public Mono<User> onSaveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/get/user/{id}")
    public Mono<User> getUser(@PathVariable("id") String id) {
        return userRepository.findById(id);
    }
}
