package philo.peanutbox.core.test_case_3

import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC3_Layer_2_2(private val tc3_layer_3_3: TC3_Layer_3_3) : TC3_ILayer_2_2 {

    override fun toString(): String {
        return "TC3_Layer_2_2(tc3_layer_3_3=$tc3_layer_3_3)"
    }
}
