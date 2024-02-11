package com.paf.exercise.exercise.exception

import java.lang.RuntimeException

class TournamentNotFoundException(id: Long? = null) : RuntimeException("$ERROR_MESSAGE $id") {

    companion object {
        const val ERROR_MESSAGE = "No tournament was found in the DB with id:"
    }
}