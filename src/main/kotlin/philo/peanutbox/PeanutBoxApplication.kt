package philo.peanutbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PeanutBoxApplication

fun main(args: Array<String>) {
	runApplication<PeanutBoxApplication>(*args)
}
