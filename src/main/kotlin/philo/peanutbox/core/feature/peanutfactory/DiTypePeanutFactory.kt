package philo.peanutbox.core.feature.peanutfactory

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import java.lang.reflect.Constructor
import java.util.*
import java.util.Arrays.stream

object DiTypePeanutFactory {

    /**
     * Peanut을 생성하거나 이미 존재하는 Peanut을 반환합니다.
     *
     * DI의 형태(생성자, 필드)에 따라 생성방식이 다릅니다.
     *
     * DFS로 동작합니다.
     */
    fun createPeanut(peanutClass: Class<*>): Any {
        val foundPeanut = findPeanut(peanutClass)
        if (isAlreadyExistPeanut(foundPeanut)) {
            return foundPeanut!!
        } else if (isDefaultConstructorInjection(peanutClass)) {
            return createPeanutByDefaultConstructor(peanutClass)
        } else if (isConstructorWithArgumentsInjection(peanutClass)) {
            return createPeanutByConstructorWithArguments(peanutClass)
        } else if (isFieldInjection(peanutClass)) {
            return createPeanutByFileInjection(peanutClass)
        }
        throw RuntimeException("허용하지 않는 DI 방식입니다. : + $peanutClass")
    }

    @Throws(Exception::class)
    private fun createPeanutByDefaultConstructor(peanutClass: Class<*>): Any {
        return getDefaultConstructor(peanutClass).newInstance()
    }

    @Throws(Exception::class)
    private fun getDefaultConstructor(peanutClass: Class<*>): Constructor<*> {
        val constructor = peanutClass.getDeclaredConstructor()
        constructor.isAccessible = true
        return constructor
    }

    @Throws(Exception::class)
    private fun createPeanutByConstructorWithArguments(peanutClass: Class<*>): Any {
        val constructors = peanutClass.declaredConstructors
        val constructor = constructors[0]
        val parameterTypes = constructor.parameterTypes
        val parameterObjects = findAndCacheConstructorArguments(parameterTypes)
        return constructor.newInstance(*parameterObjects)
    }

    /**
     *
     * 1. private 기본 생성자를 기반으로 peanut을 만듭니다
     *
     * 2. 생성할 peanut의 필드들을 만듭니다
     *
     * 3. 필드 오브젝트를 peanut에 set합니다
     *
     * 4. 해당 peanut을 반환합니다
     */
    @Throws(Exception::class)
    private fun createPeanutByFileInjection(peanutClass: Class<*>): Any {
        val hiddenDefaultConstructor = getDefaultConstructor(peanutClass)
        val newObject = hiddenDefaultConstructor.newInstance()
        val fields = peanutClass.declaredFields
        for (field in fields) {
            if (field.isAnnotationPresent(GiveMePeanut::class.java)) {
                field.isAccessible = true
                val fieldObject = ClassTypePeanutFactory.createPeanut(field.type)
                field[newObject] = fieldObject // 생성한 객체의 필드에 주입
                AutoPeanutScanner.peanuts.add(fieldObject)
            }
        }
        return newObject
    }

    /**
     * 1. peanut인자를 생성하거나 가져와서 필드 배열에 담습니다.
     *
     * 2. 가져온 peanut인자를 autoPeanutScanner의 peanuts 필드에 추가합니다.
     */
    @Throws(Exception::class)
    private fun findAndCacheConstructorArguments(argumentClasses: Array<Class<*>>): Array<Any> {
        val argumentObjects = stream(argumentClasses)
            .map { ClassTypePeanutFactory.createPeanut(it) }
            .toArray()

        AutoPeanutScanner.peanuts.addAll(argumentObjects)

        return argumentObjects
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> findPeanut(clazz: Class<T>): T? {
        return AutoPeanutScanner.peanuts.stream()
            .filter { peanut -> clazz.isAssignableFrom(peanut!!.javaClass) }
            .findAny()
            .orElse(null) as T?
    }

    private fun isAlreadyExistPeanut(peanut: Any?): Boolean {
        return peanut != null
    }

    private fun isDefaultConstructorInjection(peanutClass: Class<*>): Boolean {
        return hasDefaultConstructor(peanutClass) && hasFieldInjectionAnnotation(peanutClass).not()
    }

    private fun isFieldInjection(peanutClass: Class<*>): Boolean {
        return hasDefaultConstructor(peanutClass) && hasFieldInjectionAnnotation(peanutClass)
    }

    private fun isConstructorWithArgumentsInjection(peanutClass: Class<*>): Boolean {
        return hasConstructorWithArguments(peanutClass) && hasFieldInjectionAnnotation(peanutClass).not()
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