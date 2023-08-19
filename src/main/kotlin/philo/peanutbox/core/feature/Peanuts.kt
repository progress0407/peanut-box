package philo.peanutbox.core.feature

/**
 * 땅콩을 자체를 가지고 있으며 땅콩에 대한 CRUD 제공
 *
 * 아래의 성격을 지님
 *
 * - 싱글톤: 오로지 하나만 존재
 * - 일급 컬렉션: 땅콩들만 지님
 * - CRUD등 간단한 기능만 제공하고, 복잡한 기능 제공은 땅콩 박스(Peanut Box가 제공)
 */
object Peanuts {

    private val peanuts: MutableSet<Any> = mutableSetOf()

    fun add(peanut: Any) {
        peanuts.add(peanut)
    }

    fun addAll(peanuts: Iterable<Any>) {
        this.peanuts.addAll(peanuts)
    }

    fun addAll(peanuts: Array<Any>) {
        this.peanuts.addAll(peanuts)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> findPeanut(clazz: Class<T>): T {
        return peanuts.stream()
            .filter { peanut -> clazz.isAssignableFrom(peanut.javaClass) }
            .findAny()
            .orElse(null) as T
    }

    fun clear() {
        peanuts.clear()
    }

    fun selfInfo() {
        peanuts.joinToString(separator = "\n")
    }
}