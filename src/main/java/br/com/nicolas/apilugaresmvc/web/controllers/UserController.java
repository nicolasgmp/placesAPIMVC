package br.com.nicolas.apilugaresmvc.web.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.apilugaresmvc.api.dto.AuthDTO;
import br.com.nicolas.apilugaresmvc.api.dto.RegisterDTO;
import br.com.nicolas.apilugaresmvc.domain.entities.UserModel;
import br.com.nicolas.apilugaresmvc.domain.repositories.UserRepository;
import br.com.nicolas.apilugaresmvc.domain.services.UserService;
import br.com.nicolas.apilugaresmvc.security.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok().body("Logged with success! Token: " + token);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid RegisterDTO request) {
        this.userService.createUser(request);
        return ResponseEntity.ok().body("User created with success!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid String password, @PathVariable UUID id) {
        this.userService.updateUser(id, password);
        return ResponseEntity.ok().body("User updated with success!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted with success!");
    }
}
