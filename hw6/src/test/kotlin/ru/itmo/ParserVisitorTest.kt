package ru.itmo

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.itmo.tokenizer.Tokenizer
import ru.itmo.visitor.ParserVisitor

class ParserVisitorTest {

    @Test
    fun checkSuccessParserVisitor() {
        val tokens = Tokenizer.createTokens("3 + 4 * 2 / (1 - 5)")

        val rpn = ParserVisitor.infixToRPN(tokens)

        assertArrayEquals(
            arrayOf("3", "4", "2", "*", "1", "5", "-", "/", "+"),
            rpn.map { it.toString() }.toTypedArray()
        )
    }

    @Test
    fun checkErrorParserVisitor() {
        val tokens = Tokenizer.createTokens("3 + 4 * 2 / 1 - 5)")

        assertThrows<RuntimeException> { ParserVisitor.infixToRPN(tokens) }
    }
}
