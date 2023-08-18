package philo.peanutbox.core.test_case_1

import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC1_Layer_2_1(private val layer_3_1: TC1_Layer_3_1) {

    override fun toString(): String {
        return "TC1_Layer_2_1(layer_3_1=$layer_3_1)"
    }
}