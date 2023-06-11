package com.laurynas.uzduotis.api.controllers

import com.laurynas.uzduotis.api.dto.request.MessageRequestDTO
import com.laurynas.uzduotis.api.dto.MessageDTO
import com.laurynas.uzduotis.api.models.MessageModel
import com.laurynas.uzduotis.api.models.UserModel
import com.laurynas.uzduotis.services.MessageService
import com.laurynas.uzduotis.services.UserService
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import org.springframework.http.HttpStatus

class MessageControllerSpec extends Specification {
    UserService userService = Mock(UserService)
    MessageService messageService = Mock(MessageService)

    MessageController messageController = new MessageController(userService, messageService)

    def "should create message and return success response"() {
        given:
        def authorizationHeader = "Bearer token"
        def messageRequest = new MessageRequestDTO(message: "Hello, World!")
        def token = "token"
        def userId = 1

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: userId)

        def createdMessage = "Message Created."
        messageService.createMessage(userId, messageRequest.message)

        when:
        def response = messageController.createMessage(authorizationHeader, messageRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body.successMessage == createdMessage
    }

    def "should return 401 unauthorized status for not logged in user on createMessage"() {
        given:
        def authorizationHeader = "Bearer token"
        def messageRequest = new MessageRequestDTO(message: "Hello, World!")
        def token = "token"

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> false

        when:
        def response = messageController.createMessage(authorizationHeader, messageRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body.errorMessage == "Not logged in."
    }

    def "should return messages and success response"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true

        def messages = [
                new MessageModel(userId: 0, message: "Hello", timestamp: 1234567890),
                new MessageModel(userId: 1, message: "World", timestamp: 1234567891)
        ]
        messageService.getAllMessages() >> messages

        userService.getUser(messages[0].userId) >> new UserModel(id: 0, username: "user1")
        userService.getUser(messages[1].userId) >> new UserModel(id: 1, username: "user2")

        when:
        def response = messageController.getMessages(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body.messages.size() == 2
        response.body.messages[0] instanceof MessageDTO
        response.body.messages[0].username == "user1"
        response.body.messages[0].message == "Hello"
        response.body.messages[1].username == "user2"
        response.body.messages[1].message == "World"
    }

    def "should return 409 conflict status for not logged in user on getMessages"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> false

        when:
        def response = messageController.getMessages(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body.errorMessage == "Not logged in."
    }
}
