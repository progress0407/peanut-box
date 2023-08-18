package philo.peanutbox.core.feature

import org.reflections.Reflections
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import philo.peanutbox.core.feature.scanner.ManualPeanutScanner

object PeanutManager {

    private val peanuts: MutableSet<Any> = HashSet()

    fun init(path: String) {
        // Get the list of all .kt files in the directory
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

    /*
        fun changePeanut(oldPeanutType: Class<*>, newPeanut: Any) {
            val beforePeanut = findPeanut(oldPeanutType)
                    ?: throw RuntimeException("제거할 peanut이 존재하지 않습니다.")
            peanuts.remove(beforePeanut)
            log.info("remove peanut = {}", beforePeanut.javaClass.simpleName)
            peanuts.add(newPeanut)
            log.info("add new peanut = {}", newPeanut.javaClass.simpleName)
        }
    */

    /*
        companion object {
            private val log = LoggerFactory.getLogger(PeanutBox::class.java)
            private val manualPeanutScanner: ManualPeanutScanner = ManualPeanutScanner.instance()
            private val autoPeanutScanner: AutoPeanutScanner = AutoPeanutScanner.instance()
        }
    */
}