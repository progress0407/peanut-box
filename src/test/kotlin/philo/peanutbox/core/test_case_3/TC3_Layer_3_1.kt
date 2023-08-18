package philo.peanutbox.core.test_case_3

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class TC3_Layer_3_1 protected constructor() : TC3_ILayer_3_1 {

    @GiveMePeanut
    private val layer_4: TC3_Layer_4? = null

    override fun toString(): String {
        return "TC3_Layer_3_1(layer_4=$layer_4)"
    }


}
