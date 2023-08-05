package philo.peanutbox.core.feature

import org.reflections.Reflections
import philo.peanutbox.core.annotation.ManualPeanut
import philo.peanutbox.core.annotation.PeanutBox
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Collectors

object ManualPeanutScanner : PeanutScanner {

    override fun scan(reflections: Reflections): Set<Any> {
        val peanuts: MutableSet<Any> = HashSet()
        val peanutConfigClasses: Set<Class<*>> = reflections.getTypesAnnotatedWith(PeanutBox::class.java)
        for (peanutConfigClass in peanutConfigClasses) {
            val peanutObjects = createPeanutsFromConfig(peanutConfigClass)
            peanuts.addAll(peanutObjects)
        }
        return peanuts
    }

    @Throws(Exception::class)
    private fun createPeanutsFromConfig(peanutConfigClass: Class<*>): List<Any> {
        val peanutConfigObject = peanutConfigClass.getConstructor().newInstance()
        return Arrays.stream(peanutConfigClass.declaredMethods)
                .filter { peanutMethod: Method -> peanutMethod.isAnnotationPresent(ManualPeanut::class.java) }
                .map { peanutMethod: Method -> createPeanut(peanutConfigObject, peanutMethod) }
                .collect(Collectors.toList())
    }

    private fun createPeanut(peanutConfigObject: Any, peanutMethod: Method): Any {
        return try {
            peanutMethod.invoke(peanutConfigObject)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }
    }
}