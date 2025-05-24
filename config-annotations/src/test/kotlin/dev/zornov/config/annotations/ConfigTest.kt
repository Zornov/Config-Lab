package config.annotations.dev.zornov.config.annotations

import dev.zornov.config.annotations.Alias
import dev.zornov.config.annotations.Comment
import dev.zornov.config.annotations.CommentBlock
import dev.zornov.config.annotations.Range
import dev.zornov.config.annotations.Required
import dev.zornov.config.annotations.Sensitive
import kotlinx.serialization.Serializable
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Serializable
@CommentBlock("Server settings")
data class TestConfig(

    @Required
    @Range(min = 1.0, max = 100.0)
    @Alias("oldMaxPlayers", "legacyMax")
    @Comment("Maximum number of players")
    val maxPlayers: Int = 20,

    @Sensitive
    @Comment("Access password")
    val password: String = "secret"
)

class AnnotationTests {

    val props = TestConfig::class.memberProperties.associateBy { it.name }

    @Test
    fun `@Required is present on maxPlayers`() {
        val ann = props["maxPlayers"]?.findAnnotation<Required>()
        assertNotNull(ann, "@Required should be present on maxPlayers")
    }

    @Test
    fun `@Range has correct min and max`() {
        val ann = props["maxPlayers"]?.findAnnotation<Range>()
        assertNotNull(ann)
        assertEquals(1.0, ann.min)
        assertEquals(100.0, ann.max)
    }

    @Test
    fun `@Alias supports multiple names`() {
        val ann = props["maxPlayers"]?.findAnnotation<Alias>()
        assertNotNull(ann)
        assertContentEquals(listOf("oldMaxPlayers", "legacyMax"), ann.names.toList())
    }

    @Test
    fun `@Sensitive is present on password`() {
        val ann = props["password"]?.findAnnotation<Sensitive>()
        assertNotNull(ann)
    }

    @Test
    fun `@Comment has correct text`() {
        val ann = props["maxPlayers"]?.findAnnotation<Comment>()
        assertNotNull(ann)
        assertEquals("Maximum number of players", ann.text)
    }

    @Test
    fun `@CommentBlock is present on class`() {
        val ann = TestConfig::class.findAnnotation<CommentBlock>()
        assertNotNull(ann)
        assertEquals("Server settings", ann.text)
    }
}
