package com.laurynas.uzduotis.api.controllers

import com.laurynas.uzduotis.api.dto.LoginRequestDTO
import com.laurynas.uzduotis.api.dto.RegisterRequestDTO
import com.laurynas.uzduotis.api.dto.TokenDTO
import com.laurynas.uzduotis.api.dto.UserDTO
import com.laurynas.uzduotis.api.models.UserModel
import com.laurynas.uzduotis.other.StatusException
import com.laurynas.uzduotis.services.UserService
import org.antlr.v4.runtime.Token
import spock.lang.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class UserControllerSpec extends Specification {
    UserService userService = Mock(UserService)

    UserController userController = new UserController(userService)

    def "should create user and return success response for admin user"() {
        given:
        def authorizationHeader = "Bearer token"
        def registerRequest = new RegisterRequestDTO(username: "testuser", password: "password", admin: true)
        def token = "token"
        def adminUserId = 1

        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: adminUserId, admin: true)

        userService.createUser(registerRequest.username, registerRequest.password, registerRequest.admin)

        when:
        def response = userController.createUser(authorizationHeader, registerRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body == ["User created!"]
    }

    def "should return 403 forbidden status for non-admin user on createUser"() {
        given:
        def authorizationHeader = "Bearer token"
        def registerRequest = new RegisterRequestDTO(username: "testuser", password: "password", admin: false)
        def token = "token"
        def userId = 1

        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: userId, admin: false)

        when:
        def response = userController.createUser(authorizationHeader, registerRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.FORBIDDEN
        response.body == "Not an admin."
    }

    def "should return 401 unauthorized status for not logged in user on createUser"() {
        given:
        def authorizationHeader = "Bearer token"
        def registerRequest = new RegisterRequestDTO(username: "testuser", password: "password", admin: true)
        def token = "token"

        userService.isLoggedIn(token) >> false

        when:
        def response = userController.createUser(authorizationHeader, registerRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Not logged in."
    }

    def "should delete user and return success response for admin user"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "testuser"
        def token = "token"
        def adminUserId = 1

        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: adminUserId, admin: true)

        userService.deleteUser(username)

        when:
        def response = userController.deleteUser(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body == "Deleted."
    }

    def "should return 403 forbidden status for non-admin user on deleteUser"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "testuser"
        def token = "token"
        def userId = 1

        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: userId, admin: false)

        when:
        def response = userController.deleteUser(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.FORBIDDEN
        response.body == "Not an admin."
    }

    def "should return 401 unauthorized status for not logged in user on deleteUser"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "testuser"
        def token = "token"

        userService.isLoggedIn(token) >> false

        when:
        def response = userController.deleteUser(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Not logged in."
    }

    def "should log in user and return token for valid credentials"() {
        given:
        def loginRequest = new LoginRequestDTO(username: "testuser", password: "password")
        def token = "token"

        userService.loginUser(loginRequest.username, loginRequest.password) >> token

        def expectedToken = new TokenDTO(token)

        when:
        def response = userController.loginUser(loginRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body.getAt("token") == expectedToken.token
    }

    def "should return 401 unauthorized status for invalid credentials on loginUser"() {
        given:
        def loginRequest = new LoginRequestDTO(username: "testuser", password: "password")

        userService.loginUser(loginRequest.username, loginRequest.password) >> {
            throw new StatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.")
        }

        when:
        def response = userController.loginUser(loginRequest)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Invalid credentials."
    }

    def "should log out user and return success response"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"

        userService.logoutUser(token)

        when:
        def response = userController.logoutUser(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body == "Logged out."
    }

    def "should return 401 unauthorized status for not logged in user on logoutUser"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"

        userService.logoutUser(token) >> {
            throw new StatusException(HttpStatus.UNAUTHORIZED, "Not logged in.")
        }

        when:
        def response = userController.logoutUser(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Not logged in."
    }

    def "should return all users for admin user"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"
        def adminUserId = 2

        def user1 = new UserModel(id: 1, username: "user1", admin: false)
        def user2 = new UserModel(id: 2, username: "user2", admin: true)
        def user3 = new UserModel(id: 3, username: "user3", admin: false)
        def users = [user1, user2, user3]

        def expectedResponse = [
                new UserDTO("user1", false),
                new UserDTO("user2", true),
                new UserDTO("user3", false)
        ]

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> user2
        userService.getAllUsers() >> users

        when:
        def response = userController.getAllUsers(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body.toString() == expectedResponse.toString()
    }

    def "should return 403 forbidden status for non-admin user on getAllUsers"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"
        def userId = 1

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> new UserModel(id: userId, admin: false)

        when:
        def response = userController.getAllUsers(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.FORBIDDEN
        response.body == "Not an admin."
    }

    def "should return 401 unauthorized status for not logged in user on getAllUsers"() {
        given:
        def authorizationHeader = "Bearer token"
        def token = "token"

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> false

        when:
        def response = userController.getAllUsers(authorizationHeader)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Not logged in."
    }
}
