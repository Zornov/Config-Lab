package dev.zornov.config.example

import dev.zornov.config.annotations.*
import dev.zornov.config.annotations.sensetive.withSafe
import dev.zornov.config.core.ConfigManager
import dev.zornov.config.core.source.FileConfigSource
import dev.zornov.config.format.json.JsonConfigFormat // Новый импорт
import dev.zornov.config.format.yaml.YamlConfigFormat
import kotlinx.serialization.Serializable
import java.nio.file.Paths

@Serializable
@CommentBlock("Main server configuration block")
data class ServerConfig(

    @Required
    @Range(min = 1.0, max = 100.0)
    @Comment("Maximum number of players", position = Position.INLINE)
    val maxPlayers: Int = 20,

    @Sensitive
    @Comment("Access password (numeric)", position = Position.ABOVE)
    val password: Int = 1,

    @CommentBlock("Database config (field-level comment block)")
    val database: DatabaseConfig = DatabaseConfig(),

    val logging: LoggingConfig = LoggingConfig()
)

@Serializable
@CommentBlock("This section defines how logging works")
data class LoggingConfig(
    @Comment("Enable logging", position = Position.INLINE)
    val enabled: Boolean = true,

    @Comment("Log level", position = Position.ABOVE)
    val level: String = "INFO"
)

@Serializable
data class DatabaseConfig(
    @Comment("URL to connect to DB", position = Position.ABOVE)
    val url: String = "localhost",

    @Comment("Use secure connection", position = Position.INLINE)
    val useSSL: Boolean = true
)

suspend fun main() {
    val source = FileConfigSource(Paths.get("config"))

    val yamlManager = ConfigManager(YamlConfigFormat(), source)
    yamlManager.create("server.yml", ServerConfig())
    val loadedYaml = yamlManager.load<ServerConfig>("server")
    println("[YAML] Loaded password: ${loadedYaml.password}")

    withSafe {
        println("[YAML] Safe password: ${loadedYaml.password}")
    }

    val jsonManager = ConfigManager(JsonConfigFormat(), source)
    jsonManager.create("server.json", ServerConfig())
    val loadedJson = jsonManager.load<ServerConfig>("server")
    println("[JSON] Loaded password: ${loadedJson.password}")

    withSafe {
        println("[JSON] Safe password: ${loadedJson.password}")
    }
}
