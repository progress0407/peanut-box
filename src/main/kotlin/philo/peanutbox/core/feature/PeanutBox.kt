package philo.peanutbox.core.feature

import org.reflections.Reflections
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import philo.peanutbox.core.feature.scanner.ManualPeanutScanner

object PeanutBox {

    private val peanuts: MutableSet<Any> = HashSet()

    fun init(path: String) {
        val reflections = Reflections(path)

        val manualPeanuts = ManualPeanutScanner.scan(reflections)
        val allPeanuts = AutoPeanutScanner.scan(reflections, manualPeanuts)

        peanuts.addAll(allPeanuts)

        println("registered peanuts: \n${peanuts.joinToString(separator = "\n")}")
    }

    fun <T> findPeanut(clazz: Class<T>): T {
        return peanuts.stream()
                .filter { peanut -> clazz.isAssignableFrom(peanut.javaClass) }
                .findAny()
                .orElseThrow { RuntimeException("해당 peanut이 존재하지 않습니다.") } as T
    }

    fun clear() {
        peanuts.clear()
    }
}