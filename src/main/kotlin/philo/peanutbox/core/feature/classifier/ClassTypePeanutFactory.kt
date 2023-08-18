package philo.peanutbox.core.feature.classifier

import philo.peanutbox.core.feature.classifier.ClassTypeClassifier.isConcreteClassImplemented
import philo.peanutbox.core.feature.classifier.ClassTypeClassifier.isSimpleConcreteClass
import philo.peanutbox.core.feature.scanner.AutoPeanutScanner

object ClassTypePeanutFactory {

    /**
     * 등록하려는 Peanut이 구체 클래스인지, 인터페이스를 가지고 있는지 여부에 따라 다르게 동작하는 메서드입니다.
     *
     * DFS로 동작합니다.
     */
    @Throws(Exception::class)
    fun createPeanut(peanutClass: Class<*>): Any {
        return if (isSimpleConcreteClass(peanutClass) || isConcreteClassImplemented(peanutClass)) {
            DiTypePeanutFactory.createPeanut(peanutClass)
        } else if (ClassTypeClassifier.isInterface(peanutClass)) {
            DiTypePeanutFactory.createPeanut(getConcreteType(peanutClass))
        } else {
            throw RuntimeException("예측하지 못한 타입입니다: + $peanutClass")
        }
    }

    /**
     * 하위 클래스를 가져옵니다.
     *
     * 단, 하위 클래스가 하나인 경우만을 가정합니다.
     *
     * 즉, 스프링의 @Praimary를 고려하지 않습니다.
     */
    private fun getConcreteType(peanutClass: Class<*>): Class<*> {
        val subPeanutClasses: Array<Any> = AutoPeanutScanner.reflections!!.getSubTypesOf(peanutClass).toTypedArray()
        if (subPeanutClasses.isEmpty()) {
            throw RuntimeException("자식 클래스가 존재하지 않습니다. : $peanutClass")
        }
        if (subPeanutClasses.size >= 2) {
            throw RuntimeException("여러 하위 타입을 지원하지 않습니다. : " + subPeanutClasses.contentToString())
        }
        return subPeanutClasses[0] as Class<*>
    }

}