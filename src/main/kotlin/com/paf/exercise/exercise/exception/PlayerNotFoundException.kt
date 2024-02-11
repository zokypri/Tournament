package com.paf.exercise.exercise.exception

import java.lang.RuntimeException

class PlayerNotFoundException(id: Long? = null) : RuntimeException("$ERROR_MESSAGE $id") {

    companion object {
        const val ERROR_MESSAGE = "No player was found with id:"
    }
}