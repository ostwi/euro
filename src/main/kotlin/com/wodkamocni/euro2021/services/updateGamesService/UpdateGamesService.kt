package com.wodkamocni.euro2021.services.updateGamesService

import com.wodkamocni.euro2021.repositories.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class UpdateGamesService {
    @Autowired
    lateinit var gameRepository: GameRepository

    @Scheduled(fixedRate = REFRESH_PERIOD)
    fun updateGames() {
        print("Games data updated.\n")
        val games = FootballDataApiClient().getGames()
        gameRepository.saveAll(games)
    }

    companion object {
        const val REFRESH_PERIOD = 60 * 60 * 1000L
    }
}
