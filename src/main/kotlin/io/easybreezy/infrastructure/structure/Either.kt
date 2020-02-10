package io.easybreezy.infrastructure.structure

sealed class Either<out LEFT, out RIGHT> {
    class Left<LEFT>(val left: LEFT) : Either<LEFT, Nothing>()
    class Right<RIGHT>(val right: RIGHT) : Either<Nothing, RIGHT>()

    /**
     * Returns true if this is a Right, false otherwise.
     * @see Right
     */
    val isRight get() = this is Right<RIGHT>

    /**
     * Returns true if this is a Left, false otherwise.
     * @see Left
     */
    val isLeft get() = this is Left<LEFT>

    /**
     * Creates a Left type.
     * @see Left
     */
    fun <LEFT> left(a: LEFT) = Left(a)

    /**
     * Creates a Left type.
     * @see Right
     */
    fun <RIGHT> right(b: RIGHT) = Right(b)

    /**
     * Applies fnL if this is a Left or fnR if this is a Right.
     * @see Left
     * @see Right
     */
    fun fold(fnL: (LEFT) -> Any, fnR: (RIGHT) -> Any): Any =
        when (this) {
            is Left -> fnL(left)
            is Right -> fnR(right)
        }
}