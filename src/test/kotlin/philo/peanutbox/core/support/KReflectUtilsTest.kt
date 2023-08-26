package philo.peanutbox.core.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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

    "Should check if it has a default constructor" {
        DefCtor::class.hasDefaultConstructor shouldBe true
        FieldInjectCtor::class.hasDefaultConstructor.shouldBeFalse() // Assuming FieldInjectCtor doesn't have a default constructor
    }

    "Should verify if it has a field injection annotation" {
        FieldInjectCtor::class.hasFieldInjectionAnnotation shouldBe true
        DefCtor::class.hasFieldInjectionAnnotation.shouldBeFalse() // Assuming DefCtor doesn't have a field injection annotation
    }

    "Should check if it uses field injection" {
        FieldInjectCtor::class.isFieldInjection shouldBe true
        DefCtor::class.isFieldInjection.shouldBeFalse() // Assuming DefCtor doesn't use field injection
    }

    "Should check if it has a constructor with arguments" {
        FieldInjectCtor::class.hasConstructorWithArguments.shouldBeTrue()
        DefCtor::class.hasConstructorWithArguments.shouldBeFalse()
    }

    "Should check if it uses default constructor injection" {
        DefCtor::class.isDefaultConstructorInjection.shouldBeTrue()
        FieldInjectCtor::class.isDefaultConstructorInjection.shouldBeFalse()
    }

    "Should check if it uses constructor with arguments injection" {
        FieldInjectCtor::class.isConstructorWithArgumentsInjection.shouldBeTrue()
        DefCtor::class.isConstructorWithArgumentsInjection.shouldBeFalse()
    }

    "Should check if it's a concrete class" {
        FieldInjectCtor::class.isConcreteClass.shouldBeTrue()
        // Add another check for a non-concrete class (interface/abstract)
    }

    "Should check if it's an interface" {
        FieldInjectCtor::class.isInterface.shouldBeFalse()
        // Add another check for an interface
    }

    // ... Add more test cases as needed for other utility functions ...

})
