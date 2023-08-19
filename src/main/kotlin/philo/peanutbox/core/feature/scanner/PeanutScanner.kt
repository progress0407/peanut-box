package philo.peanutbox.core.feature.scanner

import org.reflections.Reflections

abstract class PeanutScanner {

    /**
     * Peanut Object를 생성하여 반환합니다.
     */
    abstract fun scan(reflections: Reflections)
}
