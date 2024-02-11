package com.paf.exercise.exercise.controller

import com.ninjasquad.springmockk.MockkBean
import com.paf.exercise.exercise.mock.mockTournamentDto
import com.paf.exercise.exercise.mock.mockTournamentsDto
import com.paf.exercise.exercise.model.db.Status
import com.paf.exercise.exercise.model.dto.Currency
import com.paf.exercise.exercise.model.dto.PlayerDto
import com.paf.exercise.exercise.model.dto.TournamentDto
import com.paf.exercise.exercise.service.TournamentService
import io.mockk.every
import java.nio.file.Files
import java.nio.file.Paths
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TournamentController::class)
@AutoConfigureMockMvc
class TournamentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var tournamentService: TournamentService

    @Test
    fun shouldAddTournament() {
        every { tournamentService.addTournament(any()) } returns 1L

        val filePathRequest = "__files/AddTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            post("/v1/exercise/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("1"))
    }

    @Test
    fun shouldThrowExceptionWhenValidationError() {

        val filePathRequest = "__files/AddTournamentValidationError.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            post("/v1/exercise/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(Matchers.containsString("with 2 errors:")))
    }

    @Test
    fun shouldUpdateRewardTournament() {
        every { tournamentService.updateReward(any(), any()) } returns Unit

        val filePathRequest = "__files/UpdateReward.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            put("/v1/exercise/tournaments/update-reward/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun shouldThrowValidationExceptionWhenUpdateRewardTournament() {
        val filePathRequest = "__files/UpdateRewardValidationError.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            put("/v1/exercise/tournaments//update-reward/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(Matchers.containsString("Reward must be at least 1 Euro")))
    }

    @Test
    fun shouldDeleteTournament() {
        every { tournamentService.deleteTournament(any()) } returns Unit

        mockMvc.perform(
            delete("/v1/exercise/tournaments/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun shouldFetchAllTournaments() {

        val filePathResponse = "__files/FetchAllTournaments.json"
        val responseContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathResponse)).toURI()))

        every { tournamentService.getAllTournaments() } returns mockTournamentsDto()

        mockMvc.perform(
            get("/v1/exercise/tournaments/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(responseContent))
    }

    @Test
    fun shouldFetchTournamentAndPlayers() {

        val filePathResponse = "__files/FetchTournamentAndPlayers.json"
        val responseContent = Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathResponse)).toURI()))

        every { tournamentService.getTournamentAndPlayers(any()) } returns mockTournamentDto()

        mockMvc.perform(
            get("/v1/exercise/tournaments/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(responseContent))
    }

}