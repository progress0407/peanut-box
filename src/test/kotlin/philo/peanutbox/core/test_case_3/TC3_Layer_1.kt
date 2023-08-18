package philo.peanutbox.core.test_case_3

import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC3_Layer_1(private val layer_2_1: TC3_Layer_2_1, private val layer_2_2: TC3_ILayer_2_2) {

    override fun toString(): String {
        return "TC3_Layer_1(layer_2_1=$layer_2_1, layer_2_2=$layer_2_2)"
    }
}
