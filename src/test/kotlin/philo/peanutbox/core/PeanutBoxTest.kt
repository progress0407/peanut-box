package philo.peanutbox.core

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.DisplayName
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

    @DisplayName("(Simple Case) Item Service를 땅콩 박스에 등록하고 불러온다")
    @Test
    fun register_peanut_box_0() {
        PeanutBox.init("philo.peanutbox.app")
        val itemService = PeanutBox.findPeanut(ItemService::class.java)

        assertAll(
            Executable { assertThat(itemService).isNotNull },
            Executable { assert_peanut_contains(ItemService::class.java) }
        )
    }

    @DisplayName("[Test Case 1] 트리 구조의 구체 클래스(땅콩)을 등록한다")
    @Test
    fun register_peanut_box_1() {
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

    @DisplayName("[Test Case 2] 레이어 아키텍처의 클래스들을 땅콩 박스에 등록한다 (인터페이스 포함)")
    @Test
    fun register_peanut_box_2() {
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

    @DisplayName("[Test Case 3] 인터페이스와 구체 클래스가 혼용되고 수동 Config 등록이 있는 복잡한 상황에서 땅콩 박스를 등록한다")
    @Test
    fun register_peanut_box_3() {
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