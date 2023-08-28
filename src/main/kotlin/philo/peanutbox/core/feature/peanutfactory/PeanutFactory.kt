package philo.peanutbox.core.feature.peanutfactory

import mu.KLogger
import mu.KotlinLogging
import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.Peanuts
import philo.peanutbox.core.support.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object PeanutFactory {

    private val log: KLogger = KotlinLogging.logger {}

    /**
     * Peanut을 생성하거나 이미 존재하는 Peanut을 반환합니다.
     *
     * DI의 형태(생성자, 필드)에 따라 생성방식이 다릅니다.
     *
     * DFS로 동작합니다.
     */
    fun createPeanutsRecursively(peanutClass: KClass<*>): Any {
        val foundPeanut = Peanuts.findPeanut(peanutClass.java)
        if (isAlreadyExistPeanut(foundPeanut)) {
            return foundPeanut
        }
        return createAndRegisterPeanutsByInjection(peanutClass)
    }

    private fun createAndRegisterPeanutsByInjection(peanutClass: KClass<*>): Any {
        if (peanutClass.isDefaultConstructorInjection) {
            return peanutClass.createObjectByDefaultConstructor

        } else if (peanutClass.isConstructorWithArgumentsInjection) {
            return createPeanutByConstructorWithArguments(peanutClass)

        } else if (peanutClass.isFieldInjection) {
            return createPeanutByFileInjection(peanutClass)
        }
        throw RuntimeException("허용하지 않는 DI 방식입니다. : + $peanutClass")
    }

    /**
     * 등록하려는 Peanut이 구체 클래스인지, 인터페이스를 가지고 있는지 여부에 따라 다르게 동작하는 메서드입니다.
     *
     * DFS로 동작합니다.
     */
    private fun createAndAddPeanutsByTypeRecursively(clazz: KClass<*>): Any {
        return if (clazz.isConcreteClass) {
            createPeanutsRecursively(clazz)
        } else if (clazz.isInterface) {
            createPeanutsRecursively(clazz.java.concreteType)
        } else {
            throw RuntimeException("예측하지 못한 타입입니다: + $clazz")
        }
    }

    private fun getDefaultConstructor(peanutClass: KClass<*>): KFunction<Any> {
        val constructor = peanutClass.primaryConstructor ?: throw RuntimeException("기본 생성자는 반드시 있어야 합니다")
        constructor.isAccessible = true
        return constructor
    }

    private fun createPeanutByConstructorWithArguments(peanutClass: KClass<*>): Any {
        val constructor = peanutClass.primaryConstructor
        val parameterTypes = constructor!!.parameters.map { it.type.classifier as KClass<*> }
        val parameterObjects = findAndAddConstructorArguments(parameterTypes)
        return constructor.call(*parameterObjects)
    }

    /**
     * 1. private 기본 생성자를 기반으로 peanut을 만듭니다
     *
     * 2. 생성할 peanut의 필드들을 만듭니다
     *
     * 3. 필드 오브젝트를 peanut에 set합니다
     *
     * 4. 해당 peanut을 반환합니다
     */
    private fun createPeanutByFileInjection(peanutClass: KClass<*>): Any {
        val hiddenDefaultConstructor = getDefaultConstructor(peanutClass)
        val newObject = hiddenDefaultConstructor.call()
        for (property: KProperty1<out Any, *> in peanutClass.declaredMemberProperties) {
            if (property.findAnnotation<GiveMePeanut>() != null) {
                property.isAccessible = true
                val fieldObject = createAndAddPeanutsByTypeRecursively(property::class)
                newObject.set(property, to = fieldObject)
                Peanuts.add(fieldObject)
            }
        }
        return newObject
    }

    /**
     * 1. peanut인자를 생성하거나 가져와서 필드 배열에 담습니다.
     *
     * 2. 가져온 peanut인자를 autoPeanutScanner의 peanuts 필드에 추가합니다.
     */
    private fun findAndAddConstructorArguments(classes: List<KClass<*>>): Array<Any> {
        val argumentObjects = classes.stream()
            .map { createAndAddPeanutsByTypeRecursively(it::class) }
            .toArray()
        Peanuts.addAll(argumentObjects)
        return argumentObjects
    }

    private fun isAlreadyExistPeanut(peanut: Any?): Boolean {
        return peanut != null
    }
}
