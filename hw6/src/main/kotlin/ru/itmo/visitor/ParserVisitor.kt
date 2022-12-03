package ru.itmo.visitor

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation
import ru.itmo.tokenizer.Token
import java.lang.Exception

object ParserVisitor : TokenVisitor {
    private lateinit var stack: MutableList<Token>
    private lateinit var rpn: MutableList<Token>

    override fun visit(token: NumberToken) {
        rpn.add(token)
    }

    override fun visit(token: Brace) {
        when (token.type) {
            Brace.Type.OPEN -> stack.add(token)
            Brace.Type.CLOSE -> {
                clearStack {
                    if (stack.last() is Brace) {
                        (stack.last() as Brace).type != Brace.Type.OPEN
                    } else true
                }
                stack.removeLast()
            }
        }
    }

    override fun visit(token: Operation) {
        while (stack.lastOrNull() is Operation &&
            (stack.lastOrNull() as Operation).type.priority >= token.type.priority
        ) {
            popStackToRpn()
        }
        stack.add(token)
    }

    fun infixToRPN(tokens: List<Token>): List<Token> = try {
        stack = mutableListOf()
        rpn = mutableListOf()

        for (token in tokens) {
            token.accept(this)
        }

        clearStack()

        rpn
    } catch (e: Exception) {
        throw RuntimeException("The expression is incorrect!")
    }

    private fun clearStack(condition: () -> Boolean = { true }) {
        while (stack.isNotEmpty() && condition()) {
            popStackToRpn()
        }
    }

    private fun popStackToRpn() {
        rpn.add(stack.removeLast())
    }
}
