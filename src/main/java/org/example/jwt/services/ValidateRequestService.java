package org.example.jwt.services;

import org.example.jwt.DTO.SubmissionDTO;
import org.example.jwt.DTO.UserDTO;
import org.example.jwt.model.User;
import org.example.jwt.repositories.RefreshTokenRepository;
import org.example.jwt.repositories.SubmissionRepository;
import org.example.jwt.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidateRequestService {
    final UserRepository userRepository;
    final RefreshTokenRepository refreshTokenRepo;
    final SubmissionRepository submissionRepository;
    final JwtService jwtService;
    public ValidateRequestService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepo, JwtService jwtService, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepo = refreshTokenRepo;
        this.jwtService = jwtService;
        this.submissionRepository = submissionRepository;
    }
    private User convertToUser(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(),
                userDTO.getEmail(), userDTO.getPassword());
        return user;
    }

    public boolean validateSignUp(UserDTO userDTO) {
        if(userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty() ||
        userDTO.getUsername().isEmpty())
            return false;

        return validateUserCredentials(userDTO);
    }
    // We check if the user already exists
    public boolean validateUserCredentials(UserDTO userDTO) {
        User user = convertToUser(userDTO);
        if(!userRepository.findByUserEmail(user.getUserEmail()).isEmpty())
            return false;
        return true;
    }

    public boolean validateLogin(UserDTO userDTO) {
        if(userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty())
            return false;
        return validateLoginCredentials(userDTO);
    }
    private boolean validateLoginCredentials(UserDTO userDTO) {
        String userEmail = userDTO.getEmail();
        String password = userDTO.getPassword();

        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
        if(optionalUser.isEmpty())
            return false;

        String hashedPassword = optionalUser.get().getPassword();

        return BCrypt.checkpw(password, hashedPassword);
    }

    public boolean validateRefreshRequest(String refreshToken) {
        if(refreshToken.isEmpty() ||
                !jwtService.validateRefreshToken(refreshToken))
            return false;
        return true;
    }

    public boolean validateUserInfoRequest(String accessToken) {
        if(accessToken.isEmpty() || !jwtService.validateAccessToken(accessToken))
            return false;
        return true;
    }

    public boolean validateLogout(String accessToken) {
        if(accessToken.isEmpty() || !jwtService.validateAccessToken(accessToken))
            return false;
        return true;
    }
}
