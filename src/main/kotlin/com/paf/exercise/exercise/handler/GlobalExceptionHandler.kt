package com.paf.exercise.exercise.handler

import com.paf.exercise.exercise.exception.PlayerNotFoundException
import com.paf.exercise.exercise.exception.TournamentNotFoundException
import java.util.logging.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.bind.MethodArgumentNotValidException

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = Logger.getLogger(GlobalExceptionHandler::class.java.name)



    @ExceptionHandler
    fun handleMethodArgumentNotValidException(error: MethodArgumentNotValidException): ResponseEntity<ResponseError> {
        return createError(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handlePlayerNotFoundException(error: PlayerNotFoundException): ResponseEntity<ResponseError> {
        return createError(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleTournamentNotFoundException(error: TournamentNotFoundException): ResponseEntity<ResponseError> {
        return createError(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleDataIntegrityViolationException(error: DataIntegrityViolationException): ResponseEntity<ResponseError> {
        return createError(error)
    }

    @ExceptionHandler
    fun handleGlobalException(error: Throwable): ResponseEntity<ResponseError> {
        return createError(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun createError(error: DataIntegrityViolationException): ResponseEntity<ResponseError> {
        logger.severe("Error encountered with message: ${error.message}")
        return ResponseEntity(
            ResponseError(
                errorMessage = "DataIntegrityViolationException",
                cause = null
            ),
            null,
            HttpStatus.BAD_REQUEST
        )
    }

    private fun createError(error: Throwable, responseCode: HttpStatus): ResponseEntity<ResponseError> {
        logger.severe("Error encountered with message: ${error.message}")
        return ResponseEntity(
            ResponseError(
                errorMessage = error.message,
                cause = error.cause
            ),
            null,
            responseCode
        )
    }
}

data class ResponseError(
    val errorMessage: String?,
    val cause: Throwable?,
)
