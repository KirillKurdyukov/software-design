package ru.itmo.visitor

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation
import ru.itmo.tokenizer.Token

object CalcVisitor : TokenVisitor {
    private lateinit var stack: MutableList<Int>

    override fun visit(token: NumberToken) {
        stack.add(token.number)
    }

    override fun visit(token: Brace) {
        TODO("Rpn not supported braces.")
    }

    override fun visit(token: Operation) {
        val a = stack.removeLast()
        val b = stack.removeLast()

        stack.add(token.exec(b, a))
    }

    fun calc(rpn: List<Token>): Int = try {
        stack = mutableListOf()
        for (token in rpn) {
            token.accept(this)
        }

        stack.last()
    } catch (e: Exception) {
        throw RuntimeException("The expression is incorrect!")
    }
}
