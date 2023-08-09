package philo.peanutbox.core.test_case_2

import com.fasterxml.jackson.databind.ObjectMapper
import philo.peanutbox.core.annotation.AutoPeanut
import philo.peanutbox.core.annotation.GiveMePeanut

@AutoPeanut
class UserController {

    @GiveMePeanut
    val userService: UserService? = null

    @GiveMePeanut
    val objectMapper: ObjectMapper? = null
}