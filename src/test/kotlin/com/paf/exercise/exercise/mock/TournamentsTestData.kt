package com.paf.exercise.exercise.mock

import com.paf.exercise.exercise.model.db.Player
import com.paf.exercise.exercise.model.db.Status
import com.paf.exercise.exercise.model.db.Tournament
import com.paf.exercise.exercise.model.dto.AddTournamentRequest
import com.paf.exercise.exercise.model.dto.Currency
import com.paf.exercise.exercise.model.dto.PlayerDto
import com.paf.exercise.exercise.model.dto.TournamentDto


fun mockTournamentsDto(): List<TournamentDto> {
    return listOf(
        TournamentDto(
            tournamentId = 1L,
            currency = Currency.EUR,
            rewardAmount = 4444,
            name = "PAF_1",
        ),
        TournamentDto(
            tournamentId = 2L,
            currency = Currency.EUR,
            rewardAmount = 3333,
            name = "PAF_2",
        )
    )
}

fun mockTournamentDto(): TournamentDto {
    return TournamentDto(
        tournamentId = 1L,
        currency = Currency.EUR,
        rewardAmount = 4444,
        name = "PAF_1",
        players = mockPlayersDto()
    )
}

fun mockPlayersDto(): List<PlayerDto> {
    return listOf(
        PlayerDto(
            playerId = 1L,
            firstName = "Jan",
            lastName = "Banan",
            status = Status.ACTIVE
        ),
        PlayerDto(
            playerId = 2L,
            firstName = "Janne",
            lastName = "Bannane",
            status = Status.INACTIVE
        )
    )
}

fun mockMutableIterableTournamentsList(): MutableIterable<Tournament> {
    return mutableListOf(
        Tournament(
            tournamentId = 1L,
            rewardAmount = 4444,
            currency = Currency.EUR,
            name = "PAF_1"
        ),
        Tournament(
            tournamentId = 2L,
            rewardAmount = 3333,
            currency = Currency.EUR,
            name = "PAF_2"
        )
    )
}

fun mockTournamentEntity(): Tournament {
    return Tournament(
        tournamentId = 1L,
        rewardAmount = 4444,
        currency = Currency.EUR,
        name = "PAF_1",
        players = emptyList()
    )
}

fun mockTournamentEntityWithPlayers(): Tournament {
    return Tournament(
        tournamentId = 1L,
        rewardAmount = 4444,
        currency = Currency.EUR,
        name = "PAF_1",
        players = mockPlayersEntity()
    )
}

fun mockPlayersEntity(): List<Player> {
    return listOf(
        Player(
            playerId = 1L,
            firstName = "Jan",
            lastName = "Banan",
            status = Status.ACTIVE,
            tournamentId = 1L
        ),
        Player(
            playerId = 2L,
            firstName = "Janne",
            lastName = "Bannane",
            status = Status.INACTIVE,
            tournamentId = 1L
        )
    )
}

fun mockTournamentEntityWithoutId(): Tournament {
    return Tournament(
        rewardAmount = 4444,
        currency = Currency.EUR,
        name = "PAF_1"
    )
}

fun mockTournamentRequest(): AddTournamentRequest {
    return AddTournamentRequest(
        name = "PAF_1",
        reward = 4444
    )
}
