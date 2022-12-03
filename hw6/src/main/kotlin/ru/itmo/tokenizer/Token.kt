package ru.itmo.tokenizer

import ru.itmo.visitor.TokenVisitor

interface Token {
    fun accept(tokenVisitor: TokenVisitor)
}

class NumberToken(val number: Int) : Token {
    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    override fun toString(): String = number.toString()
}

class Brace(val type: Type) : Token {
    companion object {
        fun open() = Brace(Type.OPEN)

        fun close() = Brace(Type.CLOSE)
    }

    enum class Type(val humanName: String) {
        OPEN("("), CLOSE(")")
    }

    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    override fun toString(): String = type.humanName
}

class Operation(val type: Type, val exec: (Int, Int) -> Int) : Token {
    companion object {
        fun plus() = Operation(Type.PLUS) { a, b -> a + b }

        fun minus() = Operation(Type.MINUS) { a, b -> a - b }

        fun multiply() = Operation(Type.MULTIPLY) { a, b -> a * b }

        fun divide() = Operation(Type.DIVIDE) { a, b -> a / b }
    }

    override fun accept(tokenVisitor: TokenVisitor) {
        tokenVisitor.visit(this)
    }

    enum class Type(val humanName: String, val priority: Int) {
        PLUS("+", 1),
        MINUS("-", 1),
        MULTIPLY("*", 2),
        DIVIDE("/", 2),
    }

    override fun toString(): String = type.humanName
}

object StubToken : Token {
    override fun accept(tokenVisitor: TokenVisitor) {}
}
