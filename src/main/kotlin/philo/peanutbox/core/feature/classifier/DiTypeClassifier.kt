package philo.peanutbox.core.feature.classifier

import philo.peanutbox.core.annotation.GiveMePeanut
import java.util.Arrays.stream

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

    /**
     * 해당 클래스 자체가 아닌,
     *
     * 클래스의 필드들이 필드 주입 어노테이션(GiveMePeanut)을 가지고 있는 경우
     */
    private fun hasFieldInjectionAnnotation(type: Class<*>): Boolean {
        return stream(type.getDeclaredFields())
            .anyMatch { it.isAnnotationPresent(GiveMePeanut::class.java) }
    }

    private fun hasConstructorWithArguments(peanutClass: Class<*>): Boolean {
        val constructor = peanutClass.getDeclaredConstructors()[0] // first constructor
        val parameterClasses = constructor.parameterTypes
        return parameterClasses.isNotEmpty()
    }
}
