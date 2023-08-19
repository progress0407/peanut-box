package philo.peanutbox.core.feature

import org.reflections.Reflections
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import philo.peanutbox.core.feature.scanner.ManualPeanutScanner

object PeanutBox {

    fun init(path: String) {
        val reflections = Reflections(path)

        val manualPeanuts = ManualPeanutScanner.scan(reflections)
        Peanuts.addAll(manualPeanuts)

        AutoPeanutScanner.scan(reflections)

        println("registered peanuts: \n${Peanuts.selfInfo()}")
    }

    fun <T> findPeanut(clazz: Class<T>): T {
        return Peanuts.findPeanut(clazz)
            ?: throw RuntimeException("해당 peanut이 존재하지 않습니다.")
    }

    fun clear() {
        Peanuts.clear()
    }
}