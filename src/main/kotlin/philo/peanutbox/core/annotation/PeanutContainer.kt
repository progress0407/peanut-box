package philo.peanutbox.core.annotation

/**
 * 땅콩 박스 어노테이션
 *
 * 땅콩 수동 등록을 위해 클래스 레벨에 선언한다
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PeanutContainer