package dev.zornov.config.core

import dev.zornov.config.annotations.*
import kotlinx.serialization.Serializable
import kotlin.test.*

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

class ConfigManagerTest {

    lateinit var source: InMemorySource
    lateinit var format: FakeConfigFormat
    lateinit var manager: ConfigManager

    @BeforeTest
    fun setup() {
        println("[SETUP] Creating manager with in-memory source and fake format")
        source = InMemorySource()
        format = FakeConfigFormat()
        manager = ConfigManager(format, source)
    }

    @Test
    fun `should encode annotated config correctly`() {
        println("[TEST] Encoding config with annotations")
        val config = TestConfig(maxPlayers = 42, password = "letmein")
        manager.save("config.txt", config)

        val result = source.get("config.txt")
        println("[RESULT] Saved string: $result")
        assertEquals("TestConfig(maxPlayers=42, password=letmein)", result)
    }

    @Test
    fun `should validate correct config`() {
        println("[TEST] Valid config within range")
        val config = TestConfig(maxPlayers = 100)

        manager.save("valid.txt", config)
        val loaded = manager.load<TestConfig>("valid.txt", config)

        println("[RESULT] Loaded config: $loaded")
        assertEquals(100, loaded.maxPlayers)
    }

    @Test
    fun `should fail validation if required is empty`() {
        println("[TEST] Invalid config with maxPlayers=0")
        val bad = "TestConfig(maxPlayers=0, password=secret)"
        source.write("bad.txt", bad)

        val ex = assertFailsWith<IllegalStateException> {
            manager.load<TestConfig>("bad.txt", TestConfig())
        }

        println("[EXPECTED FAILURE] ${ex.message}")
        assertTrue(ex.message!!.contains("must be in range"))
    }

    @Test
    fun `should fallback to default if missing`() {
        println("[TEST] Load fallback config if missing")
        val fallback = TestConfig(maxPlayers = 77, password = "admin")
        val loaded = manager.load("missing.txt", fallback)

        println("[RESULT] Loaded fallback: $loaded")
        assertEquals(77, loaded.maxPlayers)
        assertEquals("admin", loaded.password)

        val written = source.get("missing.txt")
        println("[RESULT] Written fallback: $written")
        assertEquals("TestConfig(maxPlayers=77, password=admin)", written)
    }
}