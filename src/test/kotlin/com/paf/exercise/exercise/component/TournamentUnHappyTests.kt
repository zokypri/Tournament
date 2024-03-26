package com.paf.exercise.exercise.component

import java.nio.file.Files
import java.nio.file.Paths
import org.hamcrest.Matchers
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
class TournamentUnHappyTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should respond BadRequest when fetching non existing tournament`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/exercise/tournaments/{tournamentId}", 111L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No tournament was found in the DB with id: 111")))
    }

    @Test
    fun `should respond BadRequest when updating non existing tournament`() {
        val filePathRequest = "__files/UpdateReward.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/tournaments//update-reward/{tournamentId}", 111L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No tournament was found in the DB with id: 111")))
    }

    @Test
    fun `should respond BadRequest when inactivating a non existing player`() {
        mockMvc.perform(
            MockMvcRequestBuilders.put("/v1/exercise/players/inactivate/{playerId}", 4L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No player was found with id: 4")))
    }

    @Test
    fun `should respond BadRequest when adding player to non existing tournament`() {
        val filePathRequest = "__files/AddPlayerToTournament.json"
        val requestContent =
            Files.readString(Paths.get(requireNotNull(javaClass.classLoader.getResource(filePathRequest)).toURI()))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/exercise/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("No tournament was found in the DB with id: 1")))
    }
}