package philo.peanutbox.core.support.sample

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.annotation.ThisIsPeanut

@ThisIsPeanut
class FieldInjectConstructorClass2 {

    @GiveMePeanut
    val sampleConcreteClass: SampleConcreteClass ?= null

    @GiveMePeanut
    val sampleInterface: SampleInterface? = null
}