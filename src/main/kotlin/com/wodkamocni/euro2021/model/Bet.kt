package com.wodkamocni.euro2021.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "bets")
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val gameId: Long,
    val userId: Long,
    val homeResult: Int,
    val awayResult: Int,
    val gameLength: GameLength,
) {
//    constructor(id: Long, gameId: Long, userId: Long, homeResult: Int, awayResult: Int, gameLength: GameLength)
}
