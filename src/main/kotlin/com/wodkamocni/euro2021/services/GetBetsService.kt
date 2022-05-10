package com.wodkamocni.euro2021.services

import com.wodkamocni.euro2021.model.Bet
import com.wodkamocni.euro2021.repositories.BetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GetBetsService {
    @Autowired
    lateinit var betRepository: BetRepository

    fun getBetsForUser(userId: Long): List<Bet> {
        return betRepository.findAll().filter { it.userId == userId }
    }
}
