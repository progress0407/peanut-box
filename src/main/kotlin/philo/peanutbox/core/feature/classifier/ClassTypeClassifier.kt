package philo.peanutbox.core.feature.classifier

object ClassTypeClassifier : TypeClassifier {

    fun isSimpleConcreteClass(clazz: Class<*>): Boolean {
        return isConcreteClass(clazz) && !hasInterfaces(clazz)
    }

    fun isInterface(clazz: Class<*>): Boolean {
        return clazz.isInterface
    }

    fun isConcreteClassImplemented(clazz: Class<*>): Boolean {
        return isConcreteClass(clazz) && hasInterfaces(clazz)
    }

    private fun isConcreteClass(clazz: Class<*>): Boolean {
        return !clazz.isInterface
    }

    private fun hasInterfaces(clazz: Class<*>): Boolean {
        return clazz.interfaces.isNotEmpty()
    }
}
