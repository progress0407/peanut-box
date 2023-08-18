package philo.peanutbox.core.feature.classifier

import philo.peanutbox.core.annotation.GiveMePeanut
import java.lang.reflect.Field
import java.util.*

object DiTypeClassifier : TypeClassifier {
    fun isDefaultConstructorInjection(peanutClass: Class<*>): Boolean {
        return hasDefaultConstructor(peanutClass) && !hasFieldInjectionAnnotation(peanutClass)
    }

    fun isFieldInjection(peanutClass: Class<*>): Boolean {
        return hasDefaultConstructor(peanutClass) && hasFieldInjectionAnnotation(peanutClass)
    }

    fun isConstructorWithArgumentsInjection(peanutClass: Class<*>): Boolean {
        return hasConstructorWithArguments(peanutClass) && !hasFieldInjectionAnnotation(peanutClass)
    }

    private fun hasDefaultConstructor(type: Class<*>): Boolean {
        return try {
            type.getDeclaredConstructor()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun hasFieldInjectionAnnotation(type: Class<*>): Boolean {
        return Arrays.stream<Field>(type.getDeclaredFields())
            .anyMatch { field: Field ->
                field.isAnnotationPresent(
                    GiveMePeanut::class.java
                )
            }
    }

    private fun hasConstructorWithArguments(peanutClass: Class<*>): Boolean {
        val constructor = peanutClass.getDeclaredConstructors()[0]
        val parameterClasses = constructor.parameterTypes
        return parameterClasses.size >= 1
    }
}
