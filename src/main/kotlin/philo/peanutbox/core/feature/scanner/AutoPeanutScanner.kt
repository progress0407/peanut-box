package philo.peanutbox.core.feature.scanner

import org.reflections.Reflections
import philo.peanutbox.core.annotation.ThisIsPeanut
import philo.peanutbox.core.feature.Peanuts
import philo.peanutbox.core.feature.peanutfactory.PeanutFactory
import java.util.*

object AutoPeanutScanner : PeanutScanner() {

    var reflections: Reflections? = null

    override fun scan(reflections: Reflections) {
        AutoPeanutScanner.reflections = reflections
        return try {
            scanInternal()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @Throws(Exception::class)
    private fun scanInternal() {
        val peanutClasses = findPeanutAnnotatedTypes(reflections)
        for (peanutClass in peanutClasses) {
            if (isNotCreationCase(peanutClass)) {
                continue
            }
            validateConstructorUnique(peanutClass)
            val newInstance = PeanutFactory.createPeanutsRecursively(peanutClass.kotlin)
            Peanuts.add(newInstance)
        }
    }

    private fun findPeanutAnnotatedTypes(reflections: Reflections?): Set<Class<*>> {
        return reflections!!.getTypesAnnotatedWith(ThisIsPeanut::class.java)
    }

    /**
     * peanutClass가 어노테이션이거나 인터페이스 구현체라면 생성하지 않습니다.
     */
    private fun isNotCreationCase(peanutClass: Class<*>): Boolean {
        return peanutClass.isAnnotation /*|| ClassTypeClassifier.isConcreteClassImplemented(peanutClass)*/
    }

    private fun validateConstructorUnique(peanutClass: Class<*>) {
        if (peanutClass.declaredConstructors.size >= 2) {
            throw RuntimeException("Peanut은 하나의 생성자만을 가져야 합니다.")
        }
    }
}