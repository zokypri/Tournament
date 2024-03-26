package com.paf.exercise.exercise.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.paf.exercise.exercise.model.dto.AddPlayerRequest
import com.paf.exercise.exercise.service.PlayerService
import java.util.logging.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class EventListener (private val playerService: PlayerService){

    private val logger = Logger.getLogger(EventListener::class.java.name)

    @KafkaListener(topics = [EXTERNAL_PLAYER_TOPIC])
    @Transactional
    fun externalPlayerListener(event: String) {
        logger.info("Received Kafka event from topic $EXTERNAL_PLAYER_TOPIC with event $event")
        val addPlayerRequest = jacksonObjectMapper().readValue(event, AddPlayerRequest::class.java)
        playerService.addPlayer(addPlayerRequest)
    }

    companion object {
        private const val EXTERNAL_PLAYER_TOPIC = "ExternalPlayer"
    }
}