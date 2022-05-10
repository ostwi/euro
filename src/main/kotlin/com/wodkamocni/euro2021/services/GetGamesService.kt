package com.wodkamocni.euro2021.services

import com.wodkamocni.euro2021.model.Game
import com.wodkamocni.euro2021.repositories.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetGamesService {
    @Autowired
    lateinit var gameRepository: GameRepository

    fun getGames(): List<Game> = gameRepository.findAll().toList()

    fun getGame(gameId: Long) = gameRepository.findById(gameId)
}
