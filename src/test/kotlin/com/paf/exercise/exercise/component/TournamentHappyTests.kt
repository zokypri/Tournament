package com.paf.exercise.exercise.component

import com.paf.exercise.exercise.model.db.Player
import com.paf.exercise.exercise.model.db.Status
import com.paf.exercise.exercise.model.db.Tournament
import com.paf.exercise.exercise.model.dto.Currency
import com.paf.exercise.exercise.repository.PlayerRepository
import com.paf.exercise.exercise.repository.TournamentRepository
import java.nio.file.Files
import java.nio.file.Paths
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@TestPropertySource(properties = [
    "spring.kafka.bootstrap-servers=localhost:9092",
    "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
    "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer"
])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EmbeddedKafka(partitions = 1, controlledShutdown = true, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
@AutoConfigureMockMvc
class TournamentHappyTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var playerRepository: PlayerRepository

    @Autowired
    private lateinit var tournamentRepository: TournamentRepository

    @Test
    fun `should handle tournament happy case`() {

        addNewTournament()

        addSecondTournament()

        fetchAllTournaments()

        addPlayerToTournament()

        addSecondPlayerToTournament()

        inactivateSecondPlayer()

        fetchTournamentAndPlayers()

        deleteSecondTournament()

        updateRewardOnTournament()

        fetchAllPlayersForOneTournament()

        val tournaments = tournamentRepository.findAll().toList()

        assertEquals(1, tournaments.size)

        assertTournament(tournaments[0])

        assertEquals(2, playerRepository.findAll().toList().size)

        deleteFirstTournament()

        assertEquals(0, tournamentRepository.findAll().toList().size)

        assertEquals(0, playerRepository.findAll().toList().size)
    }

    private fun deleteFirstTournament() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/tournaments/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun assertTournament(tournament: Tournament) {
        val players = tournament.players
        SoftAssertions().apply {
            assertThat(tournament.tournamentId).isEqualTo(1L)
            assertThat(tournament.rewardAmount).isEqualTo(5555)
            assertThat(tournament.currency).isEqualTo(Currency.EUR)
            assertThat(tournament.name).isEqualTo("PAF_1")
            assertThat(players.size).isEqualTo(2)
        }.assertAll()
        assertFirstPlayer(players[0])
        assertSecondPlayer(players[1])
    }

    private fun assertFirstPlayer(player: Player) {
        SoftAssertions().apply {
            assertThat(player.playerId).isEqualTo(1L)
            assertThat(player.firstName).isEqualTo("Jan")
            assertThat(player.lastName).isEqualTo("Banan")
            assertThat(player.status).isEqualTo(Status.ACTIVE)
            assertThat(player.tournamentId).isEqualTo(1L)
        }.assertAll()
    }

    private fun assertSecondPlayer(player: Player) {
        SoftAssertions().apply {
            assertThat(player.playerId).isEqualTo(2L)
            assertThat(player.firstName).isEqualTo("Janne")
            assertThat(player.lastName).isEqualTo("Bannane")
            assertThat(player.status).isEqualTo(Status.INACTIVE)
            assertThat(player.tournamentId).isEqualTo(1L)
        }.assertAll()
    }

    private fun fetchAllPlayersForOneTournament() {
        val filePathResponse = "__files/FetchAllPlayersForTournament.json"
        val responseContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathResponse)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/players/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(responseContent))
    }


    private fun deleteSecondTournament() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/v1/exercise/tournaments/{tournamentId}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
        val tournaments = tournamentRepository.findAll().toList()
        assertEquals(1, tournaments.size)
        assertEquals(1, tournaments[0].tournamentId)
    }

    private fun fetchTournamentAndPlayers() {
        val filePathResponse = "__files/FetchTournamentAndPlayers.json"
        val responseContent = Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathResponse)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/tournaments/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(responseContent))
    }

    private fun fetchAllTournaments() {
        val filePathResponse = "__files/FetchAllTournaments.json"
        val responseContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathResponse)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/tournaments/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(responseContent))
    }

    private fun inactivateSecondPlayer() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/players/inactivate/{playerId}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun addNewTournament() {
        val filePathRequest = "__files/AddTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("1"))
        assertTrue(tournamentRepository.findById(1L).isPresent)
    }

    private fun addSecondTournament() {
        val filePathRequest = "__files/AddSecondTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/tournaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("2"))
        assertTrue(tournamentRepository.findById(2L).isPresent)
    }

    private fun updateRewardOnTournament() {
        val filePathRequest = "__files/UpdateReward.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/tournaments//update-reward/{tournamentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
        assertEquals(tournamentRepository.findById(1L).get().rewardAmount, 5555)
    }

    private fun addPlayerToTournament() {

        val filePathRequest = "__files/AddPlayerToTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun addSecondPlayerToTournament() {

        val filePathRequest = "__files/AddSecondPlayerToTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}