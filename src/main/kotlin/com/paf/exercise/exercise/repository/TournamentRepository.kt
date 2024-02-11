package com.paf.exercise.exercise.repository

import com.paf.exercise.exercise.model.db.Tournament
import org.springframework.data.repository.CrudRepository

interface TournamentRepository: CrudRepository<Tournament, Long> {

}