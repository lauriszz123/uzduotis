package com.laurynas.uzduotis.api.controllers;

import com.laurynas.uzduotis.api.dto.*;
import com.laurynas.uzduotis.api.dto.request.LoginRequestDTO;
import com.laurynas.uzduotis.api.dto.request.RegisterRequestDTO;
import com.laurynas.uzduotis.api.dto.response.GetAllUsersResponseDTO;
import com.laurynas.uzduotis.api.dto.response.LoginResponseDTO;
import com.laurynas.uzduotis.api.dto.response.SimpleResponseDTO;
import com.laurynas.uzduotis.api.models.UserModel;
import com.laurynas.uzduotis.other.StatusException;
import com.laurynas.uzduotis.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "Endpoints for managing users/authentication")
public class UserController {

    private static final String NOT_AN_ADMIN = "Not an admin.";
    private static final String NOT_LOGGED_IN = "Not logged in.";

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SimpleResponseDTO> createUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RegisterRequestDTO registerRequest) {
        try {
            String token = authorizationHeader.substring(authorizationHeader.indexOf(" ") + 1);
            if (userService.isLoggedIn(token)) {
                UserModel user = userService.getUser(token);
                if (user.isAdmin()) {
                    userService.createUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.isAdmin());
                    SimpleResponseDTO response = new SimpleResponseDTO();
                    response.setSuccessMessage("User created!");
                    return ResponseEntity.ok(response);
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            SimpleResponseDTO response = new SimpleResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }

    @Operation(
            summary = "Delete a user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<SimpleResponseDTO> deleteUser(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("username") String username) {
        try {
            String token = authorizationHeader.substring(authorizationHeader.indexOf(" ") + 1);
            if (userService.isLoggedIn(token)) {
                if (userService.getUser(token).isAdmin()) {
                    userService.deleteUser(username);
                    SimpleResponseDTO response = new SimpleResponseDTO();
                    response.setSuccessMessage("Deleted.");
                    return ResponseEntity.ok(response);
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            SimpleResponseDTO response = new SimpleResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }

    @Operation(
            summary = "User login"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged in",
                    content = @Content(
                            schema = @Schema(
                                    implementation = LoginResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    description = "Error occurred while trying to log in",
                    content = @Content(
                            schema = @Schema(
                                    implementation = LoginResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            return ResponseEntity.ok(response);
        } catch (StatusException e) {
            LoginResponseDTO response = new LoginResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }

    @Operation(
            summary = "User logout"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged out",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<SimpleResponseDTO> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            userService.logoutUser(token);
            SimpleResponseDTO response = new SimpleResponseDTO();
            response.setSuccessMessage("Logged out.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (StatusException e) {
            SimpleResponseDTO response = new SimpleResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }

    @Operation(
            summary = "Get all users in database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched all users",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GetAllUsersResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GetAllUsersResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = GetAllUsersResponseDTO.class
                            )
                    )
            )
    })
    @GetMapping("/all")
    public ResponseEntity<GetAllUsersResponseDTO> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                if (userService.getUser(token).isAdmin()) {
                    List<UserModel> users = userService.getAllUsers();
                    List<UserDTO> returnedUsers = new ArrayList<>();

                    users.forEach(user -> returnedUsers.add(new UserDTO(user.getUsername(), user.isAdmin())));

                    GetAllUsersResponseDTO response = new GetAllUsersResponseDTO();
                    response.setUsers(returnedUsers);

                    return ResponseEntity.ok(response);
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            GetAllUsersResponseDTO response = new GetAllUsersResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }
}
