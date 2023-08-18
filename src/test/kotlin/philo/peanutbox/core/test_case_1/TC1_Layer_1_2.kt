package philo.peanutbox.core.test_case_1

import philo.peanutbox.core.annotation.AutoPeanut

@AutoPeanut
class TC1_Layer_1_2(private val layer_2_3: TC1_Layer_2_3) {

    override fun toString(): String {
        return "TC1_Layer_1_2(layer_2_3=$layer_2_3)"
    }
}