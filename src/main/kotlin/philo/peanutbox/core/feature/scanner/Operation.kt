package philo.peanutbox.core.feature.scanner

sealed class Operation {
    abstract fun execute(a: Int, b: Int): Int

    class Add : Operation() {
        override fun execute(a: Int, b: Int): Int = a + b
    }

    class Subtract : Operation() {
        override fun execute(a: Int, b: Int): Int = a - b
    }

    class Multiply : Operation() {
        override fun execute(a: Int, b: Int): Int = a * b
    }

    class Divide : Operation() {
        override fun execute(a: Int, b: Int): Int = if (b != 0) a / b else 0
    }
}

fun calculate(a: Int, b: Int, operation: Operation): Int {
    return operation.execute(a, b)
}

fun main() {
    val addition = Operation.Add()
    val subtraction = Operation.Subtract()
    val multiplication = Operation.Multiply()
    val division = Operation.Divide()

    println(calculate(10, 5, addition)) // Output: 15
    println(calculate(10, 5, subtraction)) // Output: 5
    println(calculate(10, 5, multiplication)) // Output: 50
    println(calculate(10, 5, division)) // Output: 2
}