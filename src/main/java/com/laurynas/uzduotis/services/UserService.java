package com.laurynas.uzduotis.services;

import com.laurynas.uzduotis.api.models.SessionModel;
import com.laurynas.uzduotis.api.models.UserModel;
import com.laurynas.uzduotis.other.StatusException;
import com.laurynas.uzduotis.repository.SessionRepository;
import com.laurynas.uzduotis.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public String parseAuthHeader(String authHeader) throws StatusException {
        if (authHeader.startsWith("Bearer") && authHeader != null) {
            String token = authHeader.substring(authHeader.indexOf(" ") + 1);
            if (token.length() == 36) {
                return token;
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, "Authorization does not meet the 36 character requirement.");
            }
        } else {
            throw new StatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization token.");
        }
    }

    public void createUser(String username, String password, boolean isAdmin) throws StatusException {
        if (userRepository.findByUsername(username) != null) {
            throw new StatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setAdmin(isAdmin);
    }

    public void deleteUser(String username) throws StatusException {
        if (userRepository.findByUsername(username) == null) {
            throw new StatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
        userRepository.deleteByUsername(username);
    }

    public String loginUser(String username, String password) throws StatusException {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new StatusException(HttpStatus.BAD_REQUEST, "Invalid username.");
        }

        if (!password.matches(user.getPassword())) {
            throw new StatusException(HttpStatus.UNAUTHORIZED, "Invalid password.");
        }

        long unixTimestamp = Instant.now().getEpochSecond();
        String uuid = UUID.randomUUID().toString();
        SessionModel session = sessionRepository.save(new SessionModel(uuid, user.getId(), unixTimestamp));

        return session.getToken();
    }

    public void logoutUser(String token) throws StatusException {
        SessionModel session = sessionRepository.findByToken(token);
        if (session == null) {
            throw new StatusException(HttpStatus.NOT_FOUND, "Session not found.");
        }

        sessionRepository.delete(session);
        System.out.println( "Deleted Session: " + session.getToken() );
    }

    public boolean isLoggedIn(String token) {
        return sessionRepository.findByToken(token) != null;
    }

    public UserModel getUser(String token) throws StatusException {
        SessionModel session = sessionRepository.findByToken(token);
        if (session == null) {
            throw new StatusException(HttpStatus.NOT_FOUND, "Session not found.");
        }
        return userRepository.getReferenceById(session.getUserId());
    }

    public UserModel getUser(Long id) {
        return userRepository.getReferenceById(id);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
}
