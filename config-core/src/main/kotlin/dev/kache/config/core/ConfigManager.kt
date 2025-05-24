package dev.kache.config.core

import dev.kache.config.core.ext.createInstance
import dev.kache.config.core.ext.withExt
import dev.kache.config.core.source.ConfigSource
import dev.kache.config.core.validator.ConfigValidator
import dev.kache.config.core.validator.DefaultConfigValidator
import kotlinx.serialization.serializer

class ConfigManager(
    val format: ConfigFormat,
    val source: ConfigSource,
    val validator: ConfigValidator = DefaultConfigValidator()
) {
    inline fun <reified T : Any> load(path: String): T {
        val serializer = serializer<T>()

        if (!source.exists(path)) {
            val default = createInstance<T>()
                ?: error("Config '$path' not found and no default constructor found.")
            save(path.withExt(format), default)
            return default
        }

        val text = source.read(path.withExt(format))
        val instance = format.decode(text, serializer)
        validator.validate(instance)
        return instance
    }

    inline fun <reified T : Any> load(path: String, default: T): T {
        val serializer = serializer<T>()

        if (!source.exists(path)) {
            save(path.withExt(format), default)
            return default
        }

        val text = source.read(path.withExt(format))
        val instance = format.decode(text, serializer)
        validator.validate(instance)
        return instance
    }

    inline fun <reified T : Any> save(path: String, data: T) {
        val serializer = serializer<T>()
        val text = format.encode(data, serializer)
        source.write(path.withExt(format), text)
    }
}