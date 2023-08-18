package philo.peanutbox.core.test_case_2

import com.fasterxml.jackson.databind.ObjectMapper
import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class UserController {

    @GiveMePeanut
    val userService: UserService? = null

    @GiveMePeanut
    val objectMapper: ObjectMapper? = null
}