package com.laurynas.uzduotis.api.controllers;

import com.laurynas.uzduotis.api.dto.request.MessageRequestDTO;
import com.laurynas.uzduotis.api.dto.MessageDTO;
import com.laurynas.uzduotis.api.dto.response.GetAllMessagesResponseDTO;
import com.laurynas.uzduotis.api.dto.response.SimpleResponseDTO;
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
                    responseCode = "413",
                    description = "Message length exceeds 255 character limit",
                    content = @Content(
                            schema = @Schema(
                                    implementation = SimpleResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SimpleResponseDTO> createMessage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MessageRequestDTO messageRequest) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                if(messageRequest.getMessage().length() <= 255) {
                    UserModel user = userService.getUser(token);
                    messageService.createMessage(user.getId(), messageRequest.getMessage());
                    SimpleResponseDTO response = new SimpleResponseDTO();
                    response.setSuccessMessage("Message Created.");
                    return ResponseEntity.ok(response);
                } else {
                    throw new StatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Message length exceeds 255 character limit.");
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
                                implementation = GetAllMessagesResponseDTO.class
                        )
                )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Not logged in, Authorization does not meet the 36 character requirement or Invalid authorization token",
            content = @Content(
                schema = @Schema(
                        implementation = GetAllMessagesResponseDTO.class
                )
            )
        )
    })
    public ResponseEntity<GetAllMessagesResponseDTO> getMessages(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                GetAllMessagesResponseDTO response = new GetAllMessagesResponseDTO();
                messageService.getAllMessages().forEach(message -> {
                    UserModel user = userService.getUser(message.getUserId());
                    MessageDTO messageDTO = new MessageDTO(user.getUsername(), message.getMessage(), message.getTimestamp());
                    response.addMessage(messageDTO);
                });
                return ResponseEntity.ok(response);
            } else {
                throw new StatusException(HttpStatus.UNAUTHORIZED, NOT_LOGGED_IN);
            }
        } catch (StatusException e) {
            GetAllMessagesResponseDTO response = new GetAllMessagesResponseDTO();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(response);
        }
    }
}
