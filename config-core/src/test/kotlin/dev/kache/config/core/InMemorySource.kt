package dev.kache.config.core

import dev.kache.config.core.source.ConfigSource

class InMemorySource : ConfigSource {
    val map = mutableMapOf<String, String>()

    override fun exists(path: String): Boolean = path in map
    override fun read(path: String): String = map[path] ?: error("No such path: $path")
    override fun write(path: String, value: String) {
        map[path] = value
    }

    fun get(path: String): String? = map[path]
}