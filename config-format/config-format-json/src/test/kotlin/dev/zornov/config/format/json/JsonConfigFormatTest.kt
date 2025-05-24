package dev.zornov.config.format.json

import dev.zornov.config.annotations.Comment
import dev.zornov.config.annotations.CommentBlock
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertTrue

class JsonConfigFormatTest {

    val format = JsonConfigFormat()

    @Serializable
    @CommentBlock("Server configuration in JSON")
    data class ServerConfig(
        @Comment("Displayed server name")
        val name: String = "TestServer",

        @Comment("Maximum number of players")
        val maxPlayers: Int = 32
    )

    @Test
    fun `should include comments in JSON output`() {
        val json = format.encode(ServerConfig(), ServerConfig.serializer())

        println(json)

        assertTrue(json.contains("// Server configuration in JSON"), "Missing class-level @CommentBlock")
        assertTrue(json.contains("// Displayed server name"), "Missing @Comment for 'name'")
        assertTrue(json.contains("// Maximum number of players"), "Missing @Comment for 'maxPlayers'")
        assertTrue(json.contains("\"name\": \"TestServer\""), "Missing actual field 'name'")
        assertTrue(json.contains("\"maxPlayers\": 32"), "Missing actual field 'maxPlayers'")
    }
}
