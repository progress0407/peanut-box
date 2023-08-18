package philo.peanutbox.core.feature.classifier

object ClassTypeClassifier : TypeClassifier {

    fun isSimpleConcreteClass(clazz: Class<*>): Boolean {
        return isConcreteClass(clazz) && hasInterfaces(clazz).not()
    }

    fun isInterface(clazz: Class<*>): Boolean {
        return clazz.isInterface
    }

    fun isConcreteClassImplemented(clazz: Class<*>): Boolean {
        return isConcreteClass(clazz) && hasInterfaces(clazz)
    }

    private fun isConcreteClass(clazz: Class<*>): Boolean {
        return isInterface(clazz).not()
    }

    private fun hasInterfaces(clazz: Class<*>): Boolean {
        return clazz.interfaces.isNotEmpty()
    }
}
