package com.laurynas.uzduotis.api.controllers;

import com.laurynas.uzduotis.api.dto.MessageRequestDTO;
import com.laurynas.uzduotis.api.dto.MessageResponseDTO;
import com.laurynas.uzduotis.api.models.UserModel;
import com.laurynas.uzduotis.other.StatusException;
import com.laurynas.uzduotis.services.UserService;
import com.laurynas.uzduotis.services.MessageService;
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
@RequestMapping("/api/v1/message")
@Tag(name = "Message API", description = "Endpoints for managing messages")
public class MessageController {

    private static final String NOT_LOGGED_IN = "Not logged in.";

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Operation(
            summary = "Create a message"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Message created",
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
            )
    })
    @PostMapping
    public ResponseEntity<?> createMessage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MessageRequestDTO messageRequest) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                UserModel user = userService.getUser(token);
                messageService.createMessage(user.getId(), messageRequest.getMessage());
                return ResponseEntity.ok(List.of("Message Created."));
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(
            summary = "Get all messages"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved messages",
                    content = @Content(
                            schema = @Schema(
                                    implementation = MessageResponseDTO.class
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
            )
    })
    public ResponseEntity<?> getMessages(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                List<MessageResponseDTO> messages = new ArrayList<>();
                messageService.getAllMessages().forEach(message -> {
                    UserModel user = userService.getUser(message.getUserId());
                    MessageResponseDTO response = new MessageResponseDTO(user.getUsername(), message.getMessage(), message.getTimestamp());
                    messages.add(response);
                });
                return ResponseEntity.ok(messages);
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
