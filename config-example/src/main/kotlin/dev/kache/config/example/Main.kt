package dev.kache.config.example
import dev.kache.config.annotations.*
import dev.kache.config.annotations.sensetive.withSafe
import dev.kache.config.core.ConfigManager
import dev.kache.config.core.source.FileConfigSource
import dev.kache.config.format.yaml.YamlConfigFormat
import kotlinx.serialization.Serializable
import java.nio.file.Paths

@Serializable
data class ServerConfig(
    @Required
    @Range(min = 1.0, max = 100.0)
    @Alias("oldMaxPlayers", "legacyMax")
    @Comment("Maximum number of players")
    val maxPlayers: Int = 20,

    @Sensitive
    @Comment("Access password")
    val password: Int = 1
)

suspend fun main() {
    val source = FileConfigSource(Paths.get("config"))
    val format = YamlConfigFormat()

    val manager = ConfigManager(format, source)

    manager.save("server.yml", ServerConfig())

    val fallback = ServerConfig(maxPlayers = 64, password = 1)
    val loaded = manager.load("fallback.yml", fallback)

    println(fallback.password)

    withSafe {
        println(fallback.password)
    }

    println(loaded)
}
