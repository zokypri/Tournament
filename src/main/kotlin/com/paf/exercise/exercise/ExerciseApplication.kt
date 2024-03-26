package com.paf.exercise.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExerciseApplication

fun main(args: Array<String>) {
	runApplication<ExerciseApplication>(*args)

	val swaggerUrl = "/exercise/swagger-ui/index.html#/";
	println("Swagger URL: http://localhost:8082$swaggerUrl")
}
