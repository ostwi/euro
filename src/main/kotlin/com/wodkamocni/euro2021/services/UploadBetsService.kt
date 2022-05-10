package com.wodkamocni.euro2021.services

import com.wodkamocni.euro2021.controllers.dtos.BetDataDto
import com.wodkamocni.euro2021.model.Bet
import com.wodkamocni.euro2021.repositories.BetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.time.LocalDateTime

@Service
class UploadBetsService {
    @Autowired
    lateinit var getGamesService: GetGamesService

    @Autowired
    lateinit var betRepository: BetRepository

    fun uploadBet(gameId: Long, userId: Long, betDataDto: BetDataDto) {
        val game = getGamesService.getGame(gameId).orElseThrow { IllegalArgumentException("Wrong game ID.") }

        if (LocalDateTime.now() > game.startDateTime) {
            throw IllegalStateException("It is too late to make this bet.")
        }

        val newBet = Bet(
            gameId = gameId,
            userId = userId,
            homeResult = betDataDto.homeResult,
            awayResult = betDataDto.awayResult,
            gameLength = betDataDto.gameLength
        )

        val existingBet = betRepository.findAll().filter {
            (it.userId == userId) and (it.gameId == gameId)
        }

        if (existingBet.isNotEmpty()) {
            betRepository.delete(existingBet.first())
        }

        betRepository.save(newBet)
    }
}
