package dev.zornov.config.core.source

interface ConfigSource {
    fun exists(path: String): Boolean
    fun read(path: String): String
    fun write(path: String, value: String)
}