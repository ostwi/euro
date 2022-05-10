package com.wodkamocni.euro2021.controllers

import com.wodkamocni.euro2021.controllers.dtos.BetDataDto
import com.wodkamocni.euro2021.controllers.dtos.Errors
import com.wodkamocni.euro2021.model.Bet
import com.wodkamocni.euro2021.services.GetBetsService
import com.wodkamocni.euro2021.services.LoginService
import com.wodkamocni.euro2021.services.UploadBetsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

@RestController
@Validated
@CrossOrigin(origins = ["*"])
@RequestMapping("/bets")
class BetsController {
    private val logger = LoggerFactory.getLogger("BetsControllerLogger")
    @Autowired
    lateinit var getBetsService: GetBetsService

    @Autowired
    lateinit var uploadBetsService: UploadBetsService

    @Autowired
    lateinit var loginService: LoginService

    @GetMapping("/all")
    fun getBetsForUser(@RequestHeader authorization: String): ResponseEntity<List<Bet>> {
        val token = authorization.removePrefix("Bearer ")

        return when (val userId = loginService.getAuthorizedUser(token)?.id) {
            is Long -> {
                val bets = getBetsService.getBetsForUser(userId)
                ResponseEntity.ok(bets)
            }
            else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/{gameId}")
    fun uploadBet(
        @PathVariable gameId: String,
        @RequestHeader authorization: String,
        @RequestBody betData: BetDataDto
    ): ResponseEntity<String> {
        val token = authorization.removePrefix("Bearer ")

        return when (val userId = loginService.getAuthorizedUser(token)?.id) {
            is Long -> {
                try {
                    uploadBetsService.uploadBet(gameId.toLong(), userId, betData)
                    ResponseEntity.ok("uploaded bet for $gameId")
                } catch (e: IllegalArgumentException) {
                    logger.error(e.message)
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body(Errors.WRONG_GAME_ID.name)
                } catch (e: IllegalStateException) {
                    logger.error(e.message)
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body(Errors.TOO_LATE.name)
                }
            }
            else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}
