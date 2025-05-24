package dev.zornov.config.core.source

import java.io.InputStreamReader

@Suppress("unused")
class ResourceConfigSource(
    val classLoader: ClassLoader = Thread.currentThread().contextClassLoader
) : ConfigSource {

    override fun exists(path: String): Boolean {
        return classLoader.getResource(path) != null
    }

    override fun read(path: String): String {
        val stream = classLoader.getResourceAsStream(path)
            ?: throw IllegalArgumentException("Resource '$path' not found in classpath")
        return InputStreamReader(stream).use { it.readText() }
    }

    override fun write(path: String, value: String) {
        error("Cannot write to resource path: '$path'. ResourceConfigSource is read-only.")
    }
}