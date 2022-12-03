package ru.itmo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.itmo.tokenizer.Tokenizer
import ru.itmo.visitor.ParserVisitor
import ru.itmo.visitor.PrintVisitor

class PrintVisitorTest {

    @Test
    fun checkSuccessPrintVisitor() {
        val tokens = Tokenizer.createTokens("3 + 4 * 2 / (1 - 5)")

        val rpn = ParserVisitor.infixToRPN(tokens)

        assertEquals(
            "3 4 2 * 1 5 - / +",
            PrintVisitor.printRpnExpr(rpn)
        )
    }
}
