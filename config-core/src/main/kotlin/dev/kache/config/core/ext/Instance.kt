package dev.kache.config.core.ext

inline fun <reified T : Any> createInstance(): T? {
    val ctor = T::class.constructors.find { it.parameters.all { param -> param.isOptional || param.type.isMarkedNullable } }
        ?: return null

    return ctor.callBy(emptyMap())
}