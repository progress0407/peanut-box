package philo.peanutbox.core

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import philo.peanutbox.app.item.ItemService
import philo.peanutbox.core.feature.PeanutBox
import philo.peanutbox.core.test_case_1.*
import philo.peanutbox.core.test_case_2.InMemoryUserRepository
import philo.peanutbox.core.test_case_2.UserController
import philo.peanutbox.core.test_case_2.UserService
import philo.peanutbox.core.test_case_3.*

class PeanutBoxTest {

    @AfterEach
    fun tearDown() {
        PeanutBox.clear()
    }

    @Test
    fun test() {
        PeanutBox.init("philo.peanutbox.app")
        val itemService = PeanutBox.findPeanut(ItemService::class.java)

        assertAll(
            Executable { assertThat(itemService).isNotNull },
            Executable { assert_peanut_contains(ItemService::class.java) }
        )
    }

    @Test
    fun getPeanut() {
        PeanutBox.init("philo.peanutbox.core.test_case_1")
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
        PeanutBox.init("philo.peanutbox.core.test_case_2")
        val userController: UserController = PeanutBox.findPeanut(UserController::class.java)
        assertAll(
            Executable { assert_peanut_contains(UserService::class.java) },
            Executable { assert_peanut_contains(InMemoryUserRepository::class.java) },
            Executable { assert_peanut_contains(UserController::class.java) },
            Executable { assert_peanut_contains(ObjectMapper::class.java) },
            Executable { assertThat(userController.userService).isExactlyInstanceOf(UserService::class.java) },
            Executable { assertThat(userController.objectMapper).isExactlyInstanceOf(ObjectMapper::class.java) }
        )
    }

    @Test
    fun getPeanut_3() {
        PeanutBox.init("philo.peanutbox.core.test_case_3")
        assertAll(
            Executable { assert_peanut_contains(TC3_Layer_1::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_2_1::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_2_2::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_3_1::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_3_2::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_3_3::class.java) },
            Executable { assert_peanut_contains(TC3_Layer_4::class.java) }
        )
    }

    private fun assert_peanut_contains(clazz: Class<*>) {
        assertThat(PeanutBox.findPeanut(clazz)).isInstanceOf(clazz)
    }
}