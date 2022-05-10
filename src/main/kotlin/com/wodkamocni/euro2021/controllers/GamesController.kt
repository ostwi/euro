package com.wodkamocni.euro2021.controllers

import com.wodkamocni.euro2021.model.Game
import com.wodkamocni.euro2021.services.GetGamesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@CrossOrigin(origins = ["*"])
@RequestMapping("/games")
class GamesController {

    @Autowired
    val gamesService: GetGamesService = GetGamesService()

    @GetMapping("/")
    fun getGames(): ResponseEntity<List<Game>> {
        val games = gamesService.getGames()
        return ResponseEntity.ok().body(games)
    }
}
