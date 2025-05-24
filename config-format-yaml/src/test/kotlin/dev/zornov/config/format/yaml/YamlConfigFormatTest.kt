package dev.zornov.config.format.yaml

import dev.zornov.config.annotations.Comment
import dev.zornov.config.annotations.CommentBlock
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertTrue

class YamlConfigFormatTest {

    val format = YamlConfigFormat()

    @Serializable
    @CommentBlock("-- Server configuration")
    data class ServerConfig(
        @Comment("Displayed server name")
        val name: String = "TestServer",

        @Comment("Maximum number of players")
        val maxPlayers: Int = 32
    )

    @Test
    fun `should include comments in output`() {
        val yaml = format.encode(ServerConfig(), ServerConfig.serializer())

        assertTrue(yaml.contains("# -- Server configuration"), "Missing class-level @CommentBlock")
        assertTrue(yaml.contains("# Displayed server name"), "Missing @Comment for 'name'")
        assertTrue(yaml.contains("# Maximum number of players"), "Missing @Comment for 'maxPlayers'")
        assertTrue(yaml.contains("name: \"TestServer\""), "Missing actual field 'name'")
        assertTrue(yaml.contains("maxPlayers: 32"), "Missing actual field 'maxPlayers'")

        val lines = yaml.lines()
        val nameLine = lines.indexOfFirst { it.trim().startsWith("name:") }
        val nameCommentLine = nameLine - 1
        assertTrue(
            lines[nameCommentLine].trim().startsWith("# Displayed server name"),
            "Comment for 'name' should appear directly above it"
        )
    }
}
