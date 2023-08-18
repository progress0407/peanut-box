package philo.peanutbox.core.test_case_1

import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC1_Layer_1_1(private val layer_2_1: TC1_Layer_2_1,
                    private val layer_2_2: TC1_Layer_2_2) {

    override fun toString(): String {
        return "TC1_Layer_1_1(layer_2_1=$layer_2_1, layer_2_2=$layer_2_2)"
    }
}