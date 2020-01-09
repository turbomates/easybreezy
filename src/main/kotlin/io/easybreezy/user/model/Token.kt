package io.easybreezy.user.model

class Token {

    companion object {
        fun generate(complexity: Complexity = Complexity.STRONG): String {
            val alphabet = ('A'..'Z') + ('a'..'z') + ('0'..'9').toList()

            return (1..complexity.value).map { alphabet.random() }.joinToString("")
        }
    }

    enum class Complexity(val value: Int) {
        WEAK(10),
        MEDIUM(20),
        STRONG(40)
    }
}
