package philo.peanutbox.core.feature.scanner

import org.reflections.Reflections
import philo.peanutbox.core.annotation.Peanut
import philo.peanutbox.core.annotation.PeanutContainer
import philo.peanutbox.core.feature.Peanuts
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object ManualPeanutScanner : PeanutScanner() {

    override fun scan(reflections: Reflections) {
        val peanutConfigClasses = extractPeanutConfigClasses(reflections)

        val constructedPeanuts = peanutConfigClasses.flatMap { createPeanutsFromConfig(it.kotlin) }
            .toMutableSet()

        Peanuts.addAll(constructedPeanuts)
    }

    private fun extractPeanutConfigClasses(reflections: Reflections): MutableSet<Class<*>> =
        reflections.getTypesAnnotatedWith(PeanutContainer::class.java)

    private fun createPeanutsFromConfig(peanutConfigClass: KClass<*>): List<Any> {
        val peanutConfigObject = peanutConfigClass.createInstance()
        return peanutConfigClass.declaredMemberFunctions
            .filter { it.findAnnotation<Peanut>() != null }
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