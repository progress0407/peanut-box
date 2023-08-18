package philo.peanutbox.core.test_case_3;

import philo.peanutbox.core.annotation.ManualPeanut;
import philo.peanutbox.core.annotation.PeanutBox;

@PeanutBox
public class TC3_Config {

    @ManualPeanut
    public TC3_Layer_3_3 tc3_layer_3_3() {
        return new TC3_Layer_3_3();
    }
}
