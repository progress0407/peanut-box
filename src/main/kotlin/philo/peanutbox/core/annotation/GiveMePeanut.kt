package philo.peanutbox.core.annotation

/**
 * 땅콩을 필드 주입을 하기 위해 사용한다
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GiveMePeanut 