package com.paf.exercise.exercise.service

import com.paf.exercise.exercise.exception.PlayerNotFoundException
import com.paf.exercise.exercise.model.db.Player
import com.paf.exercise.exercise.model.db.Status
import com.paf.exercise.exercise.model.dto.AddPlayerRequest
import com.paf.exercise.exercise.model.dto.PlayerDto
import com.paf.exercise.exercise.repository.PlayerRepository
import java.util.logging.Logger
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerService (private val playerRepository: PlayerRepository, private val tournamentService: TournamentService){

    private val logger = Logger.getLogger(PlayerService::class.java.name)

    @Transactional
    fun addPlayer(addOPlayerRequest: AddPlayerRequest): Long {
        val tournamentId = addOPlayerRequest.tournamentId
        tournamentService.fetchTournamentOrThrow(tournamentId)
        logger.info("Saving player to tournament with id: $tournamentId")
        val player = playerRepository.save(
            Player(
                firstName = addOPlayerRequest.firstName,
                lastName = addOPlayerRequest.lastName,
                tournamentId = tournamentId,
                status = Status.ACTIVE,
            )
        )
        logger.info("Player with id: ${player.playerId} was created for tournament with id: $tournamentId")
        return player.playerId ?: throw PlayerNotFoundException()
    }

    @Transactional
    fun inactivatePlayer(playerId: Long) {
        logger.info("Inactivating player with id: $playerId")
        playerRepository.findByIdOrNull(playerId)?.run {
            playerRepository.save(copy(status = Status.INACTIVE))
        } ?: throw PlayerNotFoundException(playerId)
        logger.info("Inactivated player with id: $playerId")
    }

    fun getPlayersByTournament(tournamentId: Long): List<PlayerDto> {
        logger.info("Fetching players from tournament with Id: $tournamentId")
        return PlayerDto.mapToPlayersDto(playerRepository.findAllByTournamentId(tournamentId))
    }
}