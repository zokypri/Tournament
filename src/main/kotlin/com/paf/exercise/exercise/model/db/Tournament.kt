package com.paf.exercise.exercise.model.db

import com.paf.exercise.exercise.model.dto.Currency
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "TOURNAMENT")
data class Tournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val tournamentId: Long? = null,

    val rewardAmount: Int,

    val currency: Currency,

    val name: String,

    @OneToMany(mappedBy = "tournament", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.EAGER)
    val players: List<Player> = emptyList(),
)
