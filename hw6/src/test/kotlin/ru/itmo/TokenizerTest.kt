package ru.itmo

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.itmo.tokenizer.Tokenizer

class TokenizerTest {

    @Test
    fun checkSuccessExecuteTokenizer() {
        val expr = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"

        assertArrayEquals(
            arrayOf(
                "(", "23", "+", "10", ")",
                "*",
                "5", "-", "3",
                "*",
                "(", "32", "+", "5", ")",
                "*",
                "(", "10", "-", "4", "*", "5", ")",
                "+",
                "8", "/", "2"
            ),
            Tokenizer
                .createTokens(expr)
                .map { it.toString() }
                .toTypedArray()
        )
    }

    @Test
    fun checkErrorExecuteTokenizer() {
        val expr = "2 + 2 $ 2"

        assertThrows<RuntimeException>("Unexpected symbol on position 7") {
            Tokenizer.createTokens(expr)
        }
    }

    @Test
    fun checkEmptyTokenizer() {
        val expr = "         "

        assertTrue(Tokenizer.createTokens(expr).isEmpty())
    }
}
