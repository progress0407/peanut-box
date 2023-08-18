package philo.peanutbox.core.test_case_3

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC3_Layer_2_1 {
    @GiveMePeanut
    private val layer_3_1: TC3_ILayer_3_1? = null

    @GiveMePeanut
    private val layer_3_2: TC3_Layer_3_2? = null
    override fun toString(): String {
        return "TC3_Layer_2_1(layer_3_1=$layer_3_1, layer_3_2=$layer_3_2)"
    }
}
