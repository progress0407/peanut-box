package philo.peanutbox.core.feature

import org.reflections.Reflections

internal interface PeanutScanner {

    fun scan(reflections: Reflections): Set<Any>
}
