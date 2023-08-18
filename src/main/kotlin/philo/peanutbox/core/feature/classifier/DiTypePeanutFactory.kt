package philo.peanutbox.core.feature.classifier

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import java.lang.reflect.Constructor
import java.util.*

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
        } else if (DiTypeClassifier.isDefaultConstructorInjection(peanutClass)) {
            return createPeanutByDefaultConstructor(peanutClass)
        } else if (DiTypeClassifier.isConstructorWithArgumentsInjection(peanutClass)) {
            return createPeanutByConstructorWithArguments(peanutClass)
        } else if (DiTypeClassifier.isFieldInjection(peanutClass)) {
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

        val argumentObjects: Array<Any> = Arrays.stream(argumentClasses)
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
}