package com.laurynas.uzduotis.api.controllers;

import com.laurynas.uzduotis.api.dto.MessageResponseDTO;
import com.laurynas.uzduotis.api.dto.MessageStatisticsDTO;
import com.laurynas.uzduotis.other.StatusException;
import com.laurynas.uzduotis.services.UserService;
import com.laurynas.uzduotis.services.StatisticsService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics API", description = "Endpoints for managing statistics")
public class StatisticsController {
    private static final String NOT_AN_ADMIN = "Not an admin.";
    private static final String NOT_LOGGED_IN = "Not logged in.";

    @Autowired
    private UserService userService;

    @Autowired
    private StatisticsService statisticsService;

    public StatisticsController(UserService userService, StatisticsService statisticsService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
    }

    @GetMapping
    @Operation(
            summary = "Get user message statistics"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved statistics",
                    content = @Content(
                            schema = @Schema(
                                    implementation = MessageStatisticsDTO.class
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
    public ResponseEntity<?> getUserStatistics(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("username") String username) {
        try {
            String token = userService.parseAuthHeader(authorizationHeader);
            if (userService.isLoggedIn(token)) {
                if (userService.getUser(token).isAdmin()) {
                    return ResponseEntity.ok(statisticsService.getUserMessagesStatistics(username));
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
