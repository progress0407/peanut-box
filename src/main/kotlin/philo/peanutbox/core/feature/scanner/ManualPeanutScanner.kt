package philo.peanutbox.core.feature.scanner

import org.reflections.Reflections
import philo.peanutbox.core.annotation.ManualPeanut
import philo.peanutbox.core.annotation.PeanutBox
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object ManualPeanutScanner : PeanutScanner {

    override fun scan(reflections: Reflections): Set<Any> {
        val peanutConfigClasses = extractPeanutConfigClasses(reflections)
        return peanutConfigClasses.flatMap { createPeanutsFromConfig(it.kotlin) }
                .toMutableSet()
    }

    private fun extractPeanutConfigClasses(reflections: Reflections): MutableSet<Class<*>> =
            reflections.getTypesAnnotatedWith(PeanutBox::class.java)

    private fun createPeanutsFromConfig(peanutConfigClass: KClass<*>): List<Any> {
        val peanutConfigObject = peanutConfigClass.createInstance()
        return peanutConfigClass.declaredMemberFunctions
                .filter { it.findAnnotation<ManualPeanut>() != null }
                .map { createPeanut(peanutConfigObject, it) }
    }

    private fun createPeanut(peanutConfigObject: Any, peanutFunction: KFunction<*>): Any {
        try {
            return peanutFunction.call(peanutConfigObject)!!
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}