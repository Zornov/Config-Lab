package dev.zornov.config.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Comment(
    val text: String,
    val position: Position = Position.ABOVE
)

enum class Position {
    ABOVE,
    INLINE
}