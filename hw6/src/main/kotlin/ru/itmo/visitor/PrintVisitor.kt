package ru.itmo.visitor

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation
import ru.itmo.tokenizer.Token

object PrintVisitor : TokenVisitor {
    private lateinit var strTokens: MutableList<String>

    override fun visit(token: NumberToken) {
        strTokens.add(token.toString())
    }

    override fun visit(token: Brace) {
        strTokens.add(token.toString())
    }

    override fun visit(token: Operation) {
        strTokens.add(token.toString())
    }

    fun printRpnExpr(rpn: List<Token>): String {
        strTokens = mutableListOf()

        rpn.forEach { it.accept(this) }
        return strTokens.joinToString(" ")
    }
}
