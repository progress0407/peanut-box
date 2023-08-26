package philo.peanutbox.core.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import philo.peanutbox.core.support.sample.*

class KReflectUtilsTest : StringSpec({


    "기본 생성자가 있는지를 검증한다" {
        DefaultConstructorClass::class.hasDefaultConstructor shouldBe true
    }

    "필드 인젝션 어노테이션이 있는지 검증한다" {
        DefaultConstructorClass::class.hasFieldInjectionAnnotation shouldBe false
        FieldInjectConstructorClass::class.hasFieldInjectionAnnotation shouldBe true
    }

    "필드 주입 생성 방식인지 검증한다" {
        DefaultConstructorClass::class.isFieldInjection shouldBe false
        FieldInjectConstructorClass::class.isFieldInjection shouldBe true
    }

    "여러 인자를 갖는 생성자인지 확인한다" {
        ArgsConstructorClass::class.hasConstructorWithArguments shouldBe true
        DefaultConstructorClass::class.hasConstructorWithArguments shouldBe false
    }

    "생성자 DI인지 검증한다" {
        DefaultConstructorClass::class.isDefaultConstructorInjection shouldBe true
        FieldInjectConstructorClass::class.isDefaultConstructorInjection shouldBe false
    }

    "여러 인자를 갖는 생성자 DI인지 검증한다" {
        ArgsConstructorClass::class.isConstructorWithArgumentsInjection shouldBe true
    }

    "구체 클래스인지 확인할 수 있다" {
        SampleConcreteClass::class.isConcreteClass shouldBe true
        SampleInterface::class.isConcreteClass shouldBe false
    }

    "인터페이스인지 확인할 수 있다" {
        SampleInterface::class.isInterface shouldBe true
        SampleConcreteClass::class.isInterface shouldBe false
    }
})
