package philo.peanutbox.core.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import philo.peanutbox.core.support.sample.DefCtor
import philo.peanutbox.core.support.sample.FieldInjectCtor

class KReflectUtilsTest : StringSpec({

    "기본 생성자가 있는지를 검증한다" {
        DefCtor::class.hasDefaultConstructor shouldBe true
    }

    "필드 인젝션 어노테이션이 있는지 검증한다" {
        FieldInjectCtor::class.hasFieldInjectionAnnotation shouldBe true
    }

    "필드 주입 생성 방식인지 검증한다" {
        FieldInjectCtor::class.isFieldInjection shouldBe true
    }
})