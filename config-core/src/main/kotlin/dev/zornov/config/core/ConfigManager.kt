package dev.zornov.config.core

import dev.zornov.config.core.ext.createInstance
import dev.zornov.config.core.ext.withExt
import dev.zornov.config.core.source.ConfigSource
import dev.zornov.config.core.validator.ConfigValidator
import dev.zornov.config.core.validator.DefaultConfigValidator
import kotlinx.serialization.serializer

class ConfigManager(
    val format: ConfigFormat,
    val source: ConfigSource,
    val validator: ConfigValidator = DefaultConfigValidator()
) {
    inline fun <reified T : Any> load(path: String): T {
        val default = createInstance<T>() ?: error("Cannot create default instance for ${T::class.simpleName}")
        return load(path, default)
    }

    inline fun <reified T : Any> load(path: String, default: T): T {
        val serializer = serializer<T>()
        val fullPath = path.withExt(format)

        if (!source.exists(fullPath)) {
            save(path, default)
            return default
        }

        val text = source.read(fullPath)
        val instance = format.decode(text, serializer)
        validator.validate(instance)
        return instance
    }

    inline fun <reified T : Any> save(path: String, data: T) {
        val serializer = serializer<T>()
        val text = format.encode(data, serializer)
        source.write(path.withExt(format), text)
    }

    inline fun <reified T: Any> create(path: String, data: T): Boolean {
        val fullPath = path.withExt(format)
        if (!source.exists(fullPath)) {
            save(path, data)
            return true
        }
        return false
    }
}