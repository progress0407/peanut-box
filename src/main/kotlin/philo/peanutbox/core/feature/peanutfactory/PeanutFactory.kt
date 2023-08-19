package philo.peanutbox.core.feature.peanutfactory

import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.Peanuts
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner
import java.lang.reflect.Constructor
import java.util.Arrays.stream

object PeanutFactory {

    /**
     * Peanut을 생성하거나 이미 존재하는 Peanut을 반환합니다.
     *
     * DI의 형태(생성자, 필드)에 따라 생성방식이 다릅니다.
     *
     * DFS로 동작합니다.
     */
    fun createPeanutsRecursively(peanutClass: Class<*>): Any {
        val foundPeanut = Peanuts.findPeanut(peanutClass)
        if (isAlreadyExistPeanut(foundPeanut)) {
            return foundPeanut!!
        }
        return createAndRegisterPeanutsByInjection(peanutClass)
    }

    private fun createAndRegisterPeanutsByInjection(peanutClass: Class<*>): Any {
        if (isDefaultConstructorInjection(peanutClass)) {
            return createPeanutByDefaultConstructor(peanutClass)

        } else if (isConstructorWithArgumentsInjection(peanutClass)) {
            return createPeanutByConstructorWithArguments(peanutClass)

        } else if (isFieldInjection(peanutClass)) {
            return createPeanutByFileInjection(peanutClass)
        }
        throw RuntimeException("허용하지 않는 DI 방식입니다. : + $peanutClass")
    }

    /**
     * 등록하려는 Peanut이 구체 클래스인지, 인터페이스를 가지고 있는지 여부에 따라 다르게 동작하는 메서드입니다.
     *
     * DFS로 동작합니다.
     */
    private fun createAndRegisterPeanutsByType(peanutClass: Class<*>): Any {
        return if (isConcreteClass(peanutClass)) {
            createPeanutsRecursively(peanutClass)
        } else if (isInterface(peanutClass)) {
            createPeanutsRecursively(getConcreteType(peanutClass))
        } else {
            throw RuntimeException("예측하지 못한 타입입니다: + $peanutClass")
        }
    }

    private fun createPeanutByDefaultConstructor(peanutClass: Class<*>): Any {
        return getDefaultConstructor(peanutClass).newInstance()
    }

    private fun getDefaultConstructor(peanutClass: Class<*>): Constructor<*> {
        val constructor = peanutClass.getDeclaredConstructor()
        constructor.isAccessible = true
        return constructor
    }

    private fun createPeanutByConstructorWithArguments(peanutClass: Class<*>): Any {
        val constructor = peanutClass.declaredConstructors[0]
        val parameterTypes = constructor.parameterTypes
        val parameterObjects = findAndCacheConstructorArguments(parameterTypes)
        return constructor.newInstance(*parameterObjects)
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
    private fun createPeanutByFileInjection(peanutClass: Class<*>): Any {
        val hiddenDefaultConstructor = getDefaultConstructor(peanutClass)
        val newObject = hiddenDefaultConstructor.newInstance()
        for (field in peanutClass.declaredFields) {
            if (field.isAnnotationPresent(GiveMePeanut::class.java)) {
                field.isAccessible = true
                val fieldObject = createAndRegisterPeanutsByType(field.type)
                field[newObject] = fieldObject // 생성한 객체의 필드에 주입
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
    private fun findAndCacheConstructorArguments(argumentClasses: Array<Class<*>>): Array<Any> {
        val argumentObjects = stream(argumentClasses)
            .map { createAndRegisterPeanutsByType(it) }
            .toArray()
        Peanuts.addAll(argumentObjects)
        return argumentObjects
    }

    /**
     * 하위 클래스를 가져옵니다.
     *
     * 단, 하위 클래스가 하나인 경우만을 가정합니다.
     *
     * 즉, 스프링의 @Praimary를 고려하지 않습니다.
     */
    private fun getConcreteType(peanutClass: Class<*>): Class<*> {
        val subPeanutClasses = AutoPeanutScanner.reflections!!.getSubTypesOf(peanutClass).toTypedArray()
        if (subPeanutClasses.isEmpty()) {
            throw RuntimeException("자식 클래스가 존재하지 않습니다. : $peanutClass")
        }
        if (subPeanutClasses.size >= 2) {
            throw RuntimeException("여러 하위 타입을 지원하지 않습니다. : " + subPeanutClasses.contentToString())
        }
        return subPeanutClasses[0] as Class<*>
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

    private fun isConcreteClass(clazz: Class<*>): Boolean {
        return isInterface(clazz).not()
    }

    private fun isInterface(clazz: Class<*>): Boolean {
        return clazz.isInterface
    }
}