package com.paf.exercise.exercise.model.dto

import com.paf.exercise.exercise.exception.PlayerNotFoundException
import com.paf.exercise.exercise.model.db.Player
import com.paf.exercise.exercise.model.db.Status
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.lang.RuntimeException

data class AddPlayerRequest(
    @field:NotNull
    val tournamentId: Long,
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
)

data class PlayerDto(
    val playerId: Long,
    val firstName: String,
    val lastName: String,
    val status: Status
){
    companion object {
        private fun mapToPlayerDto(player: Player): PlayerDto =
            PlayerDto(
                playerId = player.playerId ?: throw PlayerNotFoundException(),
                firstName = player.firstName,
                lastName = player.lastName,
                status = player.status
            )

        fun mapToPlayersDto(players: List<Player>): List<PlayerDto> =
            players.map { player -> mapToPlayerDto(player) }
    }
}
