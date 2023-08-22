package philo.peanutbox.core.support.sample

import philo.peanutbox.core.annotation.GiveMePeanut

class FieldInjectCtor() {

    @GiveMePeanut
    private var f1: Any? = null
}