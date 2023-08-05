package philo.peanutbox.core.feature

import org.reflections.Reflections

internal interface PeanutScanner {

    /**
     * Peanut Object를 생성하여 반환합니다.
     */
    fun scan(reflections: Reflections): Set<Any>
}
