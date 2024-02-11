package com.paf.exercise.exercise.repository

import com.paf.exercise.exercise.model.db.Player
import org.springframework.data.repository.CrudRepository

interface PlayerRepository: CrudRepository<Player, Long> {

    fun findAllByTournamentId(tournamentId: Long): List<Player>

}