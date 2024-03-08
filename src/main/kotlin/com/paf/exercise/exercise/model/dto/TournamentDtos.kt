package com.paf.exercise.exercise.model.dto

import com.paf.exercise.exercise.exception.TournamentNotFoundException
import com.paf.exercise.exercise.model.db.Tournament
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class TournamentOverviewDto(
    val tournamentId: Long,
    val rewardAmount: Int,
    val currency: Currency,
    val name: String,
)
data class TournamentDto(
    val tournamentId: Long,
    val rewardAmount: Int,
    val players: List<PlayerDto> = emptyList(),
    val currency: Currency,
    val name: String,
){
    companion object {
        fun mapToTournamentDto(tournament: Tournament) : TournamentDto =
            TournamentDto(
                tournamentId = tournament.tournamentId ?: throw TournamentNotFoundException(),
                rewardAmount = tournament.rewardAmount,
                players = PlayerDto.mapToPlayersDto(tournament.players),
                currency = tournament.currency,
                name = tournament.name
            )

        fun mapToTournamentSlimmedDto(tournament: Tournament) : TournamentOverviewDto =
            TournamentOverviewDto(
                tournamentId = tournament.tournamentId ?: throw TournamentNotFoundException(),
                rewardAmount = tournament.rewardAmount,
                currency = tournament.currency,
                name = tournament.name
            )
    }
}

enum class Currency {
    EUR,
    SEK
}

data class AddTournamentRequest(
    @field:NotBlank
    val name: String,
    @field:Min(value = 1, message = "Reward must be at least 1 Euro")
    val reward: Int,
)

data class UpdateTournamentRequest(
    @field:Min(value = 1, message = "Reward must be at least 1 Euro")
    val reward: Int,
)
