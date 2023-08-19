package philo.peanutbox.core.test_case_3;

import philo.peanutbox.core.annotation.Peanut;
import philo.peanutbox.core.annotation.PeanutContainer;

@PeanutContainer
public class TC3_Config {

    @Peanut
    public TC3_Layer_3_3 tc3_layer_3_3() {
        return new TC3_Layer_3_3();
    }
}
