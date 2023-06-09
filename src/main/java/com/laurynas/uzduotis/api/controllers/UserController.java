package com.laurynas.uzduotis.api.controllers;

import com.laurynas.uzduotis.api.dto.*;
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
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<?> createUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody RegisterRequestDTO registerRequest) {
        try {
            String token = authorizationHeader.substring(authorizationHeader.indexOf(" ") + 1);
            if (userService.isLoggedIn(token)) {
                UserModel user = userService.getUser(token);
                if (user.isAdmin()) {
                    userService.createUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.isAdmin());
                    return ResponseEntity.ok(List.of("User created!"));
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
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
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("username") String username) {
        try {
            String token = authorizationHeader.substring(authorizationHeader.indexOf(" ") + 1);
            if (userService.isLoggedIn(token)) {
                if (userService.getUser(token).isAdmin()) {
                    userService.deleteUser(username);
                    return ResponseEntity.ok("Deleted.");
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
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
                                    implementation = TokenDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    description = "Error occured while trying to log in",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new TokenDTO(token));
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
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
                                    implementation = TokenDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(authorizationHeader.indexOf(" ") + 1);
            userService.logoutUser(token);
            return ResponseEntity.status(HttpStatus.OK).body("Logged out.");
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
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
                                    type = "array",
                                    example = "[ { \"username\": \"simpleUsername\", \"admin\": false }, { \"username\": \"adminUser\", \"admin\": true } ]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not an admin",
                    content = @Content(
                            schema = @Schema(
                                    implementation = String.class
                            )
                    )
            )
    })
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                if (userService.getUser(token).isAdmin()) {
                    List<UserModel> users = userService.getAllUsers();
                    List<UserDTO> returnedUsers = new ArrayList<>();

                    users.forEach(user -> returnedUsers.add(new UserDTO(user.getUsername(), user.isAdmin())));

                    return ResponseEntity.ok(returnedUsers);
                } else {
                    throw new StatusException(HttpStatus.FORBIDDEN, NOT_AN_ADMIN);
                }
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
