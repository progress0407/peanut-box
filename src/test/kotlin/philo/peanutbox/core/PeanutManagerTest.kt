package philo.peanutbox.core

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import philo.peanutbox.app.item.ItemService
import philo.peanutbox.core.feature.PeanutManager
import philo.peanutbox.core.test_case_1.*
import philo.peanutbox.core.test_case_2.InMemoryUserRepository
import philo.peanutbox.core.test_case_2.UserController
import philo.peanutbox.core.test_case_2.UserService

class PeanutManagerTest {

    @AfterEach
    fun tearDown() {
        PeanutManager.clear()
    }

    @Test
    fun test() {
        PeanutManager.init("philo.peanutbox.app")
        val itemService = PeanutManager.findPeanut(ItemService::class.java)

        assertAll(
                Executable { assertThat(itemService).isNotNull },
                Executable { assertThat(itemService).isInstanceOf(ItemService::class.java) }
        )
    }

    @Test
    fun getPeanut() {
        PeanutManager.init("philo.peanutbox.core.test_case_1")
        assertAll(
                Executable { assert_peanut_contains(TC1_Layer_1_1::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_1_2::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_1_3::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_2_1::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_2_2::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_2_3::class.java) },
                Executable { assert_peanut_contains(TC1_Layer_3_1::class.java) }
        )
    }

    @Test
    fun getPeanut_2() {
        PeanutManager.init("philo.peanutbox.core.test_case_2")
        val userController: UserController = PeanutManager.findPeanut(UserController::class.java)
        assertAll(
                Executable { assert_peanut_contains(UserService::class.java) },
                Executable { assert_peanut_contains(InMemoryUserRepository::class.java) },
                Executable { assert_peanut_contains(UserController::class.java) },
                Executable { assert_peanut_contains(ObjectMapper::class.java) },
                Executable { assertThat(userController.userService).isExactlyInstanceOf(UserService::class.java) },
                Executable { assertThat(userController.objectMapper).isExactlyInstanceOf(ObjectMapper::class.java) }
        )
    }

    private fun assert_peanut_contains(clazz: Class<*>) {
        assertThat(PeanutManager.findPeanut(clazz)).isInstanceOf(clazz)
    }
}