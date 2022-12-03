package ru.itmo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.itmo.tokenizer.Tokenizer
import ru.itmo.visitor.CalcVisitor
import ru.itmo.visitor.ParserVisitor
import java.util.stream.Stream

class CalcVisitorTest {

    companion object {
        @JvmStatic
        fun correctExpression(): Stream<Arguments> = Stream.of(
            "2 / 2 + 2 * 2 - 4 * 4 + 1" to -10,
            "(123)*(123-123)" to 0,
            "3213*(73-41)+22" to 102838,
            "2 + 2 * 2" to 6,
            "2 * 2 + 2" to 6,
            "(1+2)*(3*(7-4)+2)" to 33,
            "(2 + 41230) / 41232 - 123 " to -122,
            "        123      " to 123,
            "      228  + 34 " to 262,
            "123 - 21312" to -21189,
            "123/123 + 123" to 124,
            "2 * 3 * 2" to 12,
            "(30 + 2) / 8" to 4,
            "       0    " to 0,
            "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2" to 1279
        ).map { Arguments.of(it.first, it.second) }

        @JvmStatic
        fun errorExpression(): Stream<Arguments> = Stream.of(
            "cos cos",
            "sin sin",
            "- - + 12",
            "()",
            "2 + + 2 * 2",
            "//123",
            "kek",
            "* 213 * 123 ",
            "        123   *   ",
            "      -228 -  + sin(34) ",
            "-123 + -21312 + sin cos",
            "+-123/-123 + sin-123",
            "( 1 + 2) sin",
            " 123  ! 123 "
        ).map(Arguments::of)
    }

    @ParameterizedTest(name = "Test {0}")
    @MethodSource("correctExpression")
    fun checkSuccessCalcVisitor(expr: String, expected: Int) {
        val tokens = Tokenizer.createTokens(expr)
        val rpn = ParserVisitor.infixToRPN(tokens)

        assertEquals(expected, CalcVisitor.calc(rpn))
    }

    @ParameterizedTest(name = "Test {0}")
    @MethodSource("errorExpression")
    fun checkErrorCalcVisitor(expr: String) {
        assertThrows<RuntimeException> {
            val tokens = Tokenizer.createTokens(expr)
            val rpn = ParserVisitor.infixToRPN(tokens)

            CalcVisitor.calc(rpn)
        }
    }
}
