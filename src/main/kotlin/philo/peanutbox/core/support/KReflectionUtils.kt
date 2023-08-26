package philo.peanutbox.core.support

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

/**
 * 코틀린 리플렉션 기능을 Human Readable 하고 편하게 사용하기 Utils 기능 모음 클래스
 */
abstract class KReflectUtils {
}

/**
 * 해당 클래스 자체가 아닌,
 *
 * 클래스의 필드들이 필드 주입 어노테이션(GiveMePeanut)을 가지고 있는 경우
 */
val KClass<*>.hasDefaultConstructor: Boolean
    get() = this.primaryConstructor?.parameters?.isEmpty() ?: false

/*
val KClass<*>.hasFieldInjectionAnnotation: Boolean
    get() = this.declaredMemberProperties.any { prop ->
        prop.isAccessible = true
        prop.findAnnotation<GiveMePeanut>() != null
    }
*/

val KClass<*>.hasFieldInjectionAnnotation: Boolean
    get() {
        return this.declaredMemberProperties.stream()
            .anyMatch { it.findAnnotation<GiveMePeanut>() != null }
    }

val KClass<*>.hasConstructorWithArguments: Boolean
    get() {
        val constructor = this.primaryConstructor // first constructor
        return constructor!!.parameters.isNotEmpty()
    }

val KClass<*>.isDefaultConstructorInjection: Boolean
    get() = this.hasDefaultConstructor && this.hasFieldInjectionAnnotation.not()

val KClass<*>.isFieldInjection: Boolean
    get() = this.hasDefaultConstructor && this.hasFieldInjectionAnnotation

val KClass<*>.isConstructorWithArgumentsInjection: Boolean
    get() {
        return this.hasConstructorWithArguments && this.hasFieldInjectionAnnotation.not()
    }

val KClass<*>.isConcreteClass: Boolean
    get() = this.isInterface.not()

val KClass<*>.isInterface: Boolean
    get() = this.java.isInterface

/**
 * 하위 클래스를 가져옵니다.
 *
 * 단, 하위 클래스가 하나인 경우만을 가정합니다.
 *
 * 즉, 스프링의 @Praimary를 고려하지 않습니다.
 */
val Class<*>.concreteType: KClass<*>
    get() {
        val subPeanutClasses = AutoPeanutScanner.reflections!!.getSubTypesOf(this).toTypedArray()
        if (subPeanutClasses.isEmpty()) {
            throw RuntimeException("자식 클래스가 존재하지 않습니다. : $this")
        }
        if (subPeanutClasses.size >= 2) {
            throw RuntimeException("여러 하위 타입을 지원하지 않습니다. : " + subPeanutClasses.contentToString())
        }
        return subPeanutClasses[0]::class
    }

val KClass<*>.createObjectByDefaultConstructor: Any
    get() = this.createInstance()

fun Any.set(property: KProperty1<out Any, *>, to: Any) {
    (property as KMutableProperty1<out Any, *>).setter.call(this, to)
}
