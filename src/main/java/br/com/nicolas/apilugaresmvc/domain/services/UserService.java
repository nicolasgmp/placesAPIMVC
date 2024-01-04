package br.com.nicolas.apilugaresmvc.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.nicolas.apilugaresmvc.api.dto.RegisterDTO;
import br.com.nicolas.apilugaresmvc.domain.entities.UserModel;
import br.com.nicolas.apilugaresmvc.domain.entities.enums.UserRole;
import br.com.nicolas.apilugaresmvc.domain.exceptions.DataIntegrityViolationException;
import br.com.nicolas.apilugaresmvc.domain.exceptions.UserNotFoundException;
import br.com.nicolas.apilugaresmvc.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails findByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    public UserModel findById(UUID id) {
        return this.userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Transactional
    public UserModel createUser(RegisterDTO request) {
        if (findByUsername(request.username()) != null) {
            throw new DataIntegrityViolationException("Username already in use!");
        }

        UserModel newUser = new UserModel(request.username(), request.password(), UserRole.USER);
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));

        return this.userRepository.save(newUser);
    }

    @Transactional
    public UserModel updateUser(UUID id, String password) {
        UserModel updatedUser = findById(id);

        updatedUser.setPassword(new BCryptPasswordEncoder().encode(password));

        return this.userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        UserModel user = findById(id);
        this.userRepository.delete(user);
    }
}
