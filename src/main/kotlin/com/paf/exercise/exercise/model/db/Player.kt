package com.paf.exercise.exercise.model.db

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "PLAYER")
data class Player(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val playerId: Long? = null,

    val firstName: String,

    val lastName: String,

    val status: Status,

    @Column(name = "tournament_id")
    @JsonIgnore
    val tournamentId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "tournament_id", insertable = false, updatable = false)
    val tournament: Tournament? = null,
){
    override fun toString(): String {
        return "Player(playerId=$playerId, firstName='$firstName', lastName='$lastName', status='$status', tournamentId=$tournamentId)"
    }
}

enum class Status {
    ACTIVE,
    INACTIVE
}
