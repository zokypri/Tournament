package com.paf.exercise.exercise.controller

import com.paf.exercise.exercise.handler.ResponseError
import com.paf.exercise.exercise.model.dto.AddPlayerRequest
import com.paf.exercise.exercise.model.dto.PlayerDto
import com.paf.exercise.exercise.service.PlayerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import java.util.logging.Logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/exercise/players")
class PlayerController(private val playerService: PlayerService) {

    private val logger = Logger.getLogger(PlayerController::class.java.name)

    @Operation(
        summary = "Add a player to a tournament.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Added a new player to a tournament",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            )
        ],
    )
    @PostMapping
    fun addPlayer(@Valid @RequestBody addOPlayerRequest: AddPlayerRequest): Long {
        logger.info("Received request to add new player to tournament with id: ${addOPlayerRequest.tournamentId}")
        return playerService.addPlayer(addOPlayerRequest)
    }

    @Operation(
        summary = "Inactivate a player from a tournament.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Inactivated a player from a tournament",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            )
        ],
    )
    @PutMapping("inactivate/{playerId}")
    fun inactivatePlayer(@PathVariable playerId: Long) {
        logger.info("Received request to inactivate a player with id: $playerId")
        playerService.inactivatePlayer(playerId)
    }

    @Operation(
        summary = "Fetch all players from a tournament.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Fetched all players from a tournament",
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(
                    schema = Schema(implementation = ResponseError::class)
                )]
            ),
        ],
    )
    @GetMapping("/{tournamentId}")
    fun getPlayersByTournament(@PathVariable tournamentId: Long): List<PlayerDto> {
        logger.info("Received request to fetch all players for a tournament with id: $tournamentId")
        return playerService.getPlayersByTournament(tournamentId)
    }
}