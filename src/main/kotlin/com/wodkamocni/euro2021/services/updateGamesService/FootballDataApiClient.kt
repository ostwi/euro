package com.wodkamocni.euro2021.services.updateGamesService

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.wodkamocni.euro2021.model.Game
import com.wodkamocni.euro2021.model.GameLength
import com.wodkamocni.euro2021.model.GameState
import com.wodkamocni.euro2021.model.TournamentPhase
import khttp.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FootballDataApiClient {
    fun getGames(): List<Game> {
        val matchesResponse = getGamesDataFromApi()
        val matches = Parser.default().parse(StringBuilder(matchesResponse)) as JsonObject
        return matches
            .array<JsonObject>("matches")
            ?.map { getData(it) } ?: emptyList()
    }

    private fun getGamesDataFromApi() = get(
        url = "http://api.football-data.org/v2/competitions/$COMPETITION_ID/matches",
        headers = mapOf(Pair("X-Auth-Token", API_KEY))
    ).content.decodeToString()

    private fun getData(data: JsonObject): Game {
        val id = data.long("id")!!
        val groupName = data.string("group")
        val length = getGameLength(data)

        val homeName = data.obj("homeTeam")!!.string("name")!!
        val awayName = data.obj("awayTeam")!!.string("name")!!

        val score = data.obj("score")!!
        val homeResult = getTeamResult(score, length, Team.HOME)
        val awayResult = getTeamResult(score, length, Team.AWAY)
        val startDateTime = LocalDateTime.parse(data.string("utcDate"), DateTimeFormatter.ISO_DATE_TIME)
        val state = getGameState(data)
        val phase = getTournamentPhase(data)

        return Game(
            id = id,
            homeTeamName = homeName,
            homeResult = homeResult,
            awayTeamName = awayName,
            awayResult = awayResult,
            startDateTime = startDateTime,
            gameLength = length,
            gameState = state,
            tournamentPhase = phase,
            groupName = groupName
        )
    }

    private fun getGameLength(data: JsonObject) = when (data.string("duration")) {
        "REGULAR" -> GameLength.REGULAR
        "EXTRA_TIME" -> GameLength.EXTRA_TIME
        "PENALTIES" -> GameLength.PENALTIES
        else -> GameLength.REGULAR
    }

    private fun getGameState(data: JsonObject) = when (data.string("status")) {
        "SCHEDULED" -> GameState.NOT_STARTED
        "FINISHED" -> GameState.FINISHED
        "IN_PLAY" -> GameState.PLAYING
        else -> GameState.NOT_STARTED
    }

    private fun getTournamentPhase(data: JsonObject) = when (data.string("stage")) {
        "GROUP_STAGE" -> TournamentPhase.GROUP
        "ROUND_OF_16" -> TournamentPhase.ONE_EIGHTH_FINAL
        "QUARTER_FINALS" -> TournamentPhase.QUARTER_FINAL
        "SEMI_FINALS" -> TournamentPhase.SEMI_FINAL
        "FINAL" -> TournamentPhase.FINAL
        else -> TournamentPhase.GROUP
    }

    private fun getTeamResult(score: JsonObject, length: GameLength, team: Team): Int? {
        val key = when (team) {
            Team.HOME -> "homeTeam"
            Team.AWAY -> "awayTeam"
        }

        return when (length) {
            GameLength.REGULAR -> score.obj("fullTime")?.int(key)
            GameLength.EXTRA_TIME -> score.obj("extraTime")?.int(key)
            GameLength.PENALTIES -> score.obj("penalties")?.int(key)
        }
    }

    enum class Team {
        HOME,
        AWAY
    }

    companion object {
        val API_KEY: String = System.getenv("API_KEY")
        const val COMPETITION_ID = "2018"
    }
}
