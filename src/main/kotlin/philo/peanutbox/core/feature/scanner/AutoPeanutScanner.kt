package philo.peanutbox.core.feature.scanner

import org.reflections.Reflections
import philo.peanutbox.core.annotation.AutoPeanut
import philo.peanutbox.core.annotation.GiveMePeanut
import philo.peanutbox.core.feature.classifier.ClassTypeClassifier
import philo.peanutbox.core.feature.classifier.DiTypeClassifier
import java.lang.reflect.Constructor
import java.util.*

object AutoPeanutScanner {

    private var reflections: Reflections? = null
    private val peanuts: MutableSet<Any> = mutableSetOf()


    fun scan(reflections: Reflections, peanuts: Set<Any>): Set<Any> {
        AutoPeanutScanner.peanuts.addAll(peanuts)
        AutoPeanutScanner.reflections = reflections
        return try {
            scanInternal()
            this.peanuts
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
            val newInstance = createPeanutsByClassTypeRecursively(peanutClass)
            peanuts.add(newInstance)
        }
    }

    /**
     * 등록하려는 Peanut이 구체 클래스인지, 인터페이스를 가지고 있는지 여부에 따라 다르게 동작하는 메서드입니다.
     *
     *
     * DFS로 동작합니다.
     */
    @Throws(Exception::class)
    private fun createPeanutsByClassTypeRecursively(peanutClass: Class<*>): Any {
        return if (ClassTypeClassifier.isSimpleConcreteClass(peanutClass) || ClassTypeClassifier.isConcreteClassImplemented(peanutClass)) {
            createPeanutsByInjectionTypeRecursively(peanutClass)
        }
        else if (ClassTypeClassifier.isInterface(peanutClass)) {
            createPeanutsByInjectionTypeRecursively(getConcreteType(peanutClass))
        } else {
            throw RuntimeException("예측하지 못한 타입입니다: + $peanutClass")
        }
    }

    /**
     * Peanut을 생성하거나 이미 존재하는 Peanut을 반환합니다.
     *
     * DI의 형태(생성자, 필드)에 따라 생성방식이 다릅니다.
     *
     * DFS로 동작합니다.
     */
    @Throws(Exception::class)
    private fun createPeanutsByInjectionTypeRecursively(peanutClass: Class<*>): Any {
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

    private fun findPeanutAnnotatedTypes(reflections: Reflections?): Set<Class<*>> {
        return reflections!!.getTypesAnnotatedWith(AutoPeanut::class.java)
    }

    /**
     * peanutClass가 어노테이션이거나 인터페이스 구현체라면 생성하지 않습니다.
     */
    private fun isNotCreationCase(peanutClass: Class<*>): Boolean {
        return peanutClass.isAnnotation || ClassTypeClassifier.isConcreteClassImplemented(peanutClass)
    }

    /**
     * 하위 클래스를 가져옵니다.
     *
     *
     * 단, 하위 클래스가 하나인 경우만을 가정합니다.
     *
     *
     * 즉, 스프링의 @Praimary를 고려하지 않습니다.
     */
    private fun getConcreteType(peanutClass: Class<*>): Class<*> {
        val subPeanutClasses: Array<Any> = reflections!!.getSubTypesOf(peanutClass).toTypedArray()
        if (subPeanutClasses.isEmpty()) {
            throw RuntimeException("자식 클래스가 존재하지 않습니다. : $peanutClass")
        }
        if (subPeanutClasses.size >= 2) {
            throw RuntimeException("여러 하위 타입을 지원하지 않습니다. : " + subPeanutClasses.contentToString())
        }
        return subPeanutClasses[0] as Class<*>
    }

    @Throws(Exception::class)
    private fun createPeanutByDefaultConstructor(peanutClass: Class<*>): Any {
        return getDefaultConstructor(peanutClass).newInstance()
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
                val fieldObject = createPeanutsByClassTypeRecursively(field.type)
                field[newObject] = fieldObject // 생성한 객체의 필드에 주입
                peanuts.add(fieldObject)
            }
        }
        return newObject
    }

    @Throws(Exception::class)
    private fun createPeanutByConstructorWithArguments(peanutClass: Class<*>): Any {
        val constructors = peanutClass.declaredConstructors
        val constructor = constructors[0]
        val parameterTypes = constructor.parameterTypes
        val parameterObjects = findAndSaveConstructorArguments(parameterTypes)
        return constructor.newInstance(*parameterObjects)
    }

    /**
     * 1. peanut인자를 생성하거나 가져와서 필드 배열에 담습니다.
     *
     *
     * 2. 가져온 peanut인자를 autoPeanutScanner의 peanuts 필드에 추가합니다.
     */
    @Throws(Exception::class)
    private fun findAndSaveConstructorArguments(argumentClasses: Array<Class<*>>): Array<Any> {

        val argumentObjects: Array<Any> = Arrays.stream(argumentClasses)
            .map { createPeanutsByClassTypeRecursively(it) }
            .toArray()

        peanuts.addAll(argumentObjects)

        return argumentObjects
    }

    private fun validateConstructorUnique(peanutClass: Class<*>) {
        if (peanutClass.declaredConstructors.size >= 2) {
            throw RuntimeException("Peanut은 하나의 생성자만을 가져야 합니다.")
        }
    }

    @Throws(Exception::class)
    private fun getDefaultConstructor(peanutClass: Class<*>): Constructor<*> {
        val constructor = peanutClass.getDeclaredConstructor()
        constructor.isAccessible = true
        return constructor
    }

    private fun isAlreadyExistPeanut(peanut: Any?): Boolean {
        return peanut != null
    }

    private fun <T> findPeanut(clazz: Class<T>): T? {
        return peanuts.stream()
                .filter { peanut: Any? -> clazz.isAssignableFrom(peanut!!.javaClass) }
                .findAny()
                .orElse(null) as T?
    }
}