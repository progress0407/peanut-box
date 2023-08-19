package philo.peanutbox.core.test_case_2

import com.fasterxml.jackson.databind.ObjectMapper
import philo.peanutbox.core.annotation.Peanut
import philo.peanutbox.core.annotation.PeanutContainer

@PeanutContainer
class PeanutConfig {

    @Peanut
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}