package com.laurynas.uzduotis.api.controllers

import com.laurynas.uzduotis.services.StatisticsService
import com.laurynas.uzduotis.services.UserService
import com.laurynas.uzduotis.api.models.UserModel
import com.laurynas.uzduotis.api.dto.MessageStatisticsDTO
import spock.lang.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class StatisticsControllerSpec extends Specification {
    UserService userService = Mock(UserService)
    StatisticsService statisticsService = Mock(StatisticsService)

    StatisticsController statisticsController = new StatisticsController(userService, statisticsService)

    def "should return user statistics for admin user"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "adminUser"
        def token = "token"

        def user = Mock(UserModel)
        user.isAdmin() >> true

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> user

        def expectedStatistics = Mock(MessageStatisticsDTO)

        statisticsService.getUserMessagesStatistics(username) >> expectedStatistics

        when:
        def response = statisticsController.getUserStatistics(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.OK
        response.body == expectedStatistics
    }


    def "should return 401 unauthorized status for not logged in user"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "user"
        def token = "token"

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> false

        when:
        def response = statisticsController.getUserStatistics(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.UNAUTHORIZED
        response.body == "Not logged in."
    }

    def "should return 403 forbidden status for non-admin user"() {
        given:
        def authorizationHeader = "Bearer token"
        def username = "user"
        def token = "token"

        def user = Mock(UserModel)
        user.isAdmin() >> false

        userService.parseAuthHeader(authorizationHeader) >> token
        userService.isLoggedIn(token) >> true
        userService.getUser(token) >> user

        when:
        def response = statisticsController.getUserStatistics(authorizationHeader, username)

        then:
        response instanceof ResponseEntity
        response.statusCode == HttpStatus.FORBIDDEN
        response.body == "Not an admin."
    }
}
