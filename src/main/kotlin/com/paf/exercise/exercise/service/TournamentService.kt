package com.paf.exercise.exercise.service

import com.paf.exercise.exercise.exception.TournamentNotFoundException
import com.paf.exercise.exercise.model.db.Tournament
import com.paf.exercise.exercise.model.dto.AddTournamentRequest
import com.paf.exercise.exercise.model.dto.Currency
import com.paf.exercise.exercise.model.dto.TournamentDto
import com.paf.exercise.exercise.model.dto.UpdateTournamentRequest
import com.paf.exercise.exercise.repository.TournamentRepository
import java.util.logging.Logger
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TournamentService(private val tournamentRepository: TournamentRepository) {

    private val logger = Logger.getLogger(TournamentService::class.java.name)

    @Transactional
    fun addTournament(addTournamentRequest: AddTournamentRequest): Long {
        logger.info("Creating new tournament with name: ${addTournamentRequest.name}")
        val tournament = tournamentRepository.save(
            Tournament(
                rewardAmount = addTournamentRequest.reward,
                currency = Currency.EUR,
                name = addTournamentRequest.name
            )
        )
        logger.info("Tournament with id: ${tournament.tournamentId} was created")
        return tournament.tournamentId ?: throw TournamentNotFoundException() // this should NEVER happen!
    }

    fun getAllTournaments(): List<TournamentDto> {
        logger.info("Fetch all tournaments")
        return tournamentRepository
            .findAll()
            .toList()
            .map { tournament ->
                TournamentDto.mapToTournamentSlimmedDto(tournament)
            }
    }

    fun getTournamentAndPlayers(tournamentId: Long): TournamentDto {
        logger.info("Fetch tournament with id: $tournamentId including all participating players")
        return TournamentDto.mapToTournamentDto(
            tournamentRepository.findByIdOrNull(tournamentId)
                ?: throw TournamentNotFoundException(tournamentId)
        )
    }

    @Transactional
    fun updateReward(tournamentId: Long, updateTournamentRequest: UpdateTournamentRequest) {
        logger.info("Updating tournament with id: $tournamentId")
        tournamentRepository.findByIdOrNull(tournamentId)?.run {
            tournamentRepository.save(copy(rewardAmount = updateTournamentRequest.reward))
            logger.info("Tournament with id: $tournamentId was updated")
        } ?: throw TournamentNotFoundException(tournamentId)
    }

    @Transactional
    fun deleteTournament(tournamentId: Long) {
        logger.info("Deleting tournament with id: $tournamentId")
        tournamentRepository.deleteById(tournamentId)
        logger.info("Deleted tournament with id: $tournamentId")
    }
}