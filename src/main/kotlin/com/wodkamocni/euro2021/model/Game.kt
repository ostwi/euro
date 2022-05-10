package com.wodkamocni.euro2021.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "games")
data class Game(
    @Id
    val id: Long,
    @Column(name = "home_team_name")
    val homeTeamName: String,
    @Column(name = "away_team_name")
    val awayTeamName: String,
    @Column(name = "home_result")
    val homeResult: Int?,
    @Column(name = "away_result")
    val awayResult: Int?,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "start_date_time")
    val startDateTime: LocalDateTime,
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "game_state")
    val gameState: GameState,
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "game_length")
    val gameLength: GameLength,
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tournament_phase")
    val tournamentPhase: TournamentPhase,
    @Column(name = "group_name")
    val groupName: String?,
)
