package dev.kache.config.core.source

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class FileConfigSource(val baseDir: Path) : ConfigSource {
    override fun exists(path: String): Boolean = baseDir.resolve(path).exists()

    override fun read(path: String): String = baseDir.resolve(path).readText()

    override fun write(path: String, value: String) {
        val file = baseDir.resolve(path)
        if (!file.parent.exists()) file.parent.createDirectories()
        file.writeText(value)
    }
}