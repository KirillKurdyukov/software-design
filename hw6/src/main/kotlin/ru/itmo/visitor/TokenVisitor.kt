package ru.itmo.visitor

import ru.itmo.tokenizer.Brace
import ru.itmo.tokenizer.NumberToken
import ru.itmo.tokenizer.Operation

interface TokenVisitor {

    fun visit(token: NumberToken)

    fun visit(token: Brace)

    fun visit(token: Operation)
}
