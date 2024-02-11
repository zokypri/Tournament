package com.paf.exercise.exercise.service

import com.paf.exercise.exercise.exception.TournamentNotFoundException
import com.paf.exercise.exercise.mock.mockMutableIterableTournamentsList
import com.paf.exercise.exercise.mock.mockTournamentDto
import com.paf.exercise.exercise.mock.mockTournamentEntity
import com.paf.exercise.exercise.mock.mockTournamentEntityWithPlayers
import com.paf.exercise.exercise.mock.mockTournamentEntityWithoutId
import com.paf.exercise.exercise.mock.mockTournamentRequest
import com.paf.exercise.exercise.mock.mockTournamentsDto
import com.paf.exercise.exercise.model.dto.UpdateTournamentRequest
import com.paf.exercise.exercise.repository.TournamentRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
class TournamentServiceTest {

    @MockK
    private lateinit var tournamentRepository: TournamentRepository

    @InjectMockKs
    private lateinit var tournamentService: TournamentService

    @Test
    fun shouldAddTournament() {
        every { tournamentRepository.save(any()) } returns mockTournamentEntity()
        assertEquals(1L, tournamentService.addTournament(mockTournamentRequest()))
    }

    @Test
    fun shouldThrowExceptionWhenAddingTournamentAndNoIdCreated() {
        every { tournamentRepository.save(any()) } returns mockTournamentEntityWithoutId()
        assertThatThrownBy {
            tournamentService.addTournament(mockTournamentRequest())
        }.isInstanceOf(TournamentNotFoundException::class.java)
    }

    @Test
    fun shouldFetchAllTournaments() {
        every { tournamentRepository.findAll() } returns mockMutableIterableTournamentsList()
        assertEquals(mockTournamentsDto(), tournamentService.getAllTournaments())
    }

    @Test
    fun shouldThrowExceptionWhenFetchingAllTournamentAndNoIdCreated() {
        every { tournamentRepository.findAll() } returns mutableListOf(mockTournamentEntityWithoutId())
        assertThatThrownBy {
            tournamentService.getAllTournaments()
        }.isInstanceOf(TournamentNotFoundException::class.java)
    }

    @Test
    fun shouldFetchTournamentAndPlayers() {
        every { tournamentRepository.findByIdOrNull(any()) } returns mockTournamentEntityWithPlayers()
        assertEquals(mockTournamentDto(), tournamentService.getTournamentAndPlayers(1L))
    }

    @Test
    fun shouldThrowExceptionWhenFetchingTournamentAndPlayersNotFound() {
        every { tournamentRepository.findByIdOrNull(any()) } returns null
        assertThatThrownBy {
            tournamentService.getTournamentAndPlayers(1L)
        }.isInstanceOf(TournamentNotFoundException::class.java)
    }

    @Test
    fun shouldUpdateTournament() {
        every { tournamentRepository.findByIdOrNull(any()) } returns mockTournamentEntityWithPlayers()
        every { tournamentRepository.save(any()) } returns mockTournamentEntity()
        tournamentService.updateReward(1L, UpdateTournamentRequest(reward = 4444))
        verify(exactly = 1) { tournamentRepository.save(any()) }
    }

    @Test
    fun shouldThrowExceptionWhenUpdatingTournamentButNotFound() {
        every { tournamentRepository.findByIdOrNull(any()) } returns null
        assertThatThrownBy {
            tournamentService.updateReward(1L, UpdateTournamentRequest(reward = 4444))
        }.isInstanceOf(TournamentNotFoundException::class.java)
    }
}