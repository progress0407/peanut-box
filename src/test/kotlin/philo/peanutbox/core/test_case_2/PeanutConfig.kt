package philo.peanutbox.core.test_case_2

import com.fasterxml.jackson.databind.ObjectMapper
import philo.peanutbox.core.annotation.ManualPeanut
import philo.peanutbox.core.annotation.PeanutBox

@PeanutBox
class PeanutConfig {

    @ManualPeanut
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}