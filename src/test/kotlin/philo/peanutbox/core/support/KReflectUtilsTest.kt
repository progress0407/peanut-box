package philo.peanutbox.core.support

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import philo.peanutbox.core.support.sample.DefCtor
import philo.peanutbox.core.support.sample.FieldInjectCtor

class KReflectUtilsTest {

    @DisplayName("기본 생성자가 있는지를 검증한다")
    @Test
    fun hasDefaultConstructor() {
        assertThat(DefCtor::class.hasDefaultConstructor).isTrue()
    }

    @DisplayName("필드 인젝션이 있는지를 검증한다")
    @Test
    fun hasFieldInjectionAnnotation() {
        assertThat(FieldInjectCtor::class.isFieldInjection).isTrue()
    }
}