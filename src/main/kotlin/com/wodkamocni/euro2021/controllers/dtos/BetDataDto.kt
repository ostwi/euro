package com.wodkamocni.euro2021.controllers.dtos

import com.wodkamocni.euro2021.model.GameLength

data class BetDataDto(
    val homeResult: Int,
    val awayResult: Int,
    val gameLength: GameLength,
)
