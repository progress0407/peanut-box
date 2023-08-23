package philo.peanutbox.core

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import philo.peanutbox.app.item.ItemService
import philo.peanutbox.core.feature.PeanutBox
import philo.peanutbox.core.test_case_1.*
import philo.peanutbox.core.test_case_2.InMemoryUserRepository
import philo.peanutbox.core.test_case_2.UserController
import philo.peanutbox.core.test_case_2.UserService
import philo.peanutbox.core.test_case_3.*

class PeanutBoxTest : StringSpec({

    afterTest {
        PeanutBox.clear()
    }

    "(Simple Case) Item Service를 땅콩 박스에 등록하고 불러온다" {
        PeanutBox.init("philo.peanutbox.app")
        val itemService = PeanutBox.findPeanut(ItemService::class.java)

        itemService.shouldBeInstanceOf<ItemService>()
        PeanutBox.findPeanut(ItemService::class.java).shouldBeInstanceOf<ItemService>()
    }

    "[Test Case 1] 트리 구조의 구체 클래스(땅콩)을 등록한다" {
        PeanutBox.init("philo.peanutbox.core.test_case_1")

        with(PeanutBox) {
            findPeanut(TC1_Layer_1_1::class.java).shouldBeInstanceOf<TC1_Layer_1_1>()
            findPeanut(TC1_Layer_1_2::class.java).shouldBeInstanceOf<TC1_Layer_1_2>()
            findPeanut(TC1_Layer_1_3::class.java).shouldBeInstanceOf<TC1_Layer_1_3>()
            findPeanut(TC1_Layer_2_1::class.java).shouldBeInstanceOf<TC1_Layer_2_1>()
            findPeanut(TC1_Layer_2_2::class.java).shouldBeInstanceOf<TC1_Layer_2_2>()
            findPeanut(TC1_Layer_2_3::class.java).shouldBeInstanceOf<TC1_Layer_2_3>()
            findPeanut(TC1_Layer_3_1::class.java).shouldBeInstanceOf<TC1_Layer_3_1>()
        }
    }

    "[Test Case 2] 레이어 아키텍처의 클래스들을 땅콩 박스에 등록한다 (인터페이스 포함)" {
        PeanutBox.init("philo.peanutbox.core.test_case_2")

        val userController: UserController = PeanutBox.findPeanut(UserController::class.java)

        with(PeanutBox) {
            findPeanut(UserService::class.java).shouldBeInstanceOf<UserService>()
            findPeanut(InMemoryUserRepository::class.java).shouldBeInstanceOf<InMemoryUserRepository>()
            findPeanut(UserController::class.java).shouldBeInstanceOf<UserController>()
            findPeanut(ObjectMapper::class.java).shouldBeInstanceOf<ObjectMapper>()
        }
        userController.userService.shouldBeInstanceOf<UserService>()
        userController.objectMapper.shouldBeInstanceOf<ObjectMapper>()
    }

    "[Test Case 3] 인터페이스와 구체 클래스가 혼용되고 수동 Config 등록이 있는 복잡한 상황에서 땅콩 박스를 등록한다" {
        PeanutBox.init("philo.peanutbox.core.test_case_3")

        with(PeanutBox) {
            findPeanut(TC3_Layer_1::class.java).shouldBeInstanceOf<TC3_Layer_1>()
            findPeanut(TC3_Layer_2_1::class.java).shouldBeInstanceOf<TC3_Layer_2_1>()
            findPeanut(TC3_Layer_2_2::class.java).shouldBeInstanceOf<TC3_Layer_2_2>()
            findPeanut(TC3_Layer_3_1::class.java).shouldBeInstanceOf<TC3_Layer_3_1>()
            findPeanut(TC3_Layer_3_2::class.java).shouldBeInstanceOf<TC3_Layer_3_2>()
            findPeanut(TC3_Layer_3_3::class.java).shouldBeInstanceOf<TC3_Layer_3_3>()
            findPeanut(TC3_Layer_4::class.java).shouldBeInstanceOf<TC3_Layer_4>()
        }
    }
})