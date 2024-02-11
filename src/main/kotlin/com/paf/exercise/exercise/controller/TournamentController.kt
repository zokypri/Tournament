package com.paf.exercise.exercise.controller

import com.paf.exercise.exercise.model.dto.TournamentDto
import com.paf.exercise.exercise.model.dto.AddTournamentRequest
import com.paf.exercise.exercise.model.dto.UpdateTournamentRequest
import com.paf.exercise.exercise.service.TournamentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import java.util.logging.Logger
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/exercise/tournaments")
class TournamentController (val tournamentService: TournamentService){

    private val logger = Logger.getLogger(TournamentController::class.java.name)

    @Operation(
        summary = "Add a tournament.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Added a new tournament",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
            ),
        ],
    )
    @PostMapping
    fun addTournament(@Valid @RequestBody tournamentRequest: AddTournamentRequest): Long {
        logger.info("Received request to add new tournament")
        return tournamentService.addTournament(tournamentRequest)
    }

    @Operation(
        summary = "Update tournament with new reward",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Updated tournament with new reward",
            ),
        ],
    )
    @PutMapping("/update-reward/{tournamentId}")
    fun updateReward(@PathVariable tournamentId: Long,@Valid @RequestBody updateTournamentRequest: UpdateTournamentRequest) {
        logger.info("Received request to update tournament with id: $tournamentId")
        tournamentService.updateReward(tournamentId, updateTournamentRequest)
    }

    @Operation(
        summary = "Delete  tournament.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Deleted  tournament",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
            ),
        ],
    )
    @DeleteMapping("/{tournamentId}")
    fun deleteTournament(@PathVariable tournamentId: Long) {
        logger.info("Received request to delete tournament with id: $tournamentId")
        tournamentService.deleteTournament(tournamentId)
    }

    @Operation(
        summary = "Fetch all tournaments.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Fetched all tournaments",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
            ),
        ],
    )
    @GetMapping("/all")
    fun getAllTournaments(): List<TournamentDto> {
        logger.info("Received request to fetch all tournaments")
        return tournamentService.getAllTournaments()
    }

    @Operation(
        summary = "Fetch tournament by id.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Fetched a tournament",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
            ),
        ],
    )
    @GetMapping("/{tournamentId}")
    fun getTournamentAndPlayers(@PathVariable tournamentId: Long): TournamentDto {
        logger.info("Received request to fetch one tournament")
        return tournamentService.getTournamentAndPlayers(tournamentId)
    }
}