package dev.kache.config.core

import dev.kache.config.annotations.*
import dev.kache.config.annotations.sensetive.withSafe
import kotlinx.serialization.Serializable

@Serializable
@CommentBlock("Server settings")
data class TestConfig(
    @Required
    @Range(min = 1.0, max = 100.0)
    @Alias("oldMaxPlayers", "legacyMax")
    @Comment("Maximum number of players")
    val maxPlayers: Int = 2000,

    @Sensitive
    @Comment("Access password")
    val password: String = "test"
)

suspend fun main() {
    val config = TestConfig()
    println(config.password)

    withSafe {
        println(config.password)
    }
}