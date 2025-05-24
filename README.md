# ConfigLab

[![GitHub stars](https://img.shields.io/github/stars/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/stargazers)
[![License](https://img.shields.io/github/license/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/blob/main/LICENSE)
[![Contributors](https://img.shields.io/github/contributors/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/graphs/contributors)

> Annotation-driven, configuration library for Kotlin

---

## Features

| Feature                 | Description                                                                                                 |
|-------------------------|-------------------------------------------------------------------------------------------------------------|
| **Declarative models**  | Plain Kotlin `data class` + `@Serializable`.                                                                |
| **Validation**          | Built-in validation via `@Required`, `@Range`, or custom annotations.                                       |
| **Sensitive redaction** | Redacts `@Sensitive` fields unless explicitly revealed inside `withSafe {}`. Works only with Gradle plugin. |
| **Rich comments**       | Support for inline (`@Comment(..., INLINE)`) and block-level (`@CommentBlock`) YAML comments.               |
| **Nested comments**     | Automatically places comments above nested `data class` blocks — supported via annotation or class-level.   |
| **Aliases**             | Support for legacy field names via `@Alias("oldName")`.                                                     |
| **Pluggable formats**   | YAML support via module; other formats can be plugged in.                                                   |
| **Multiple sources**    | Load from files or any custom source implementation.                                                        |

---

## Quickstart

### Add Dependencies

<details>
<summary><strong>Gradle (Kotlin DSL)</strong></summary>

```kotlin
plugins {
    // Enable @Sensitive support
    id("dev.zornov.config.sensitive") version "1.0" 
}

dependencies {
    // Core runtime
    implementation("dev.zornov.config:core:1.0")

    // Annotations
    implementation("dev.zornov.config:annotations:1.0")

    // YAML formatter
    implementation("dev.zornov.config.format:yml:1.0")
}
````

</details>

---

## Full example

Browse the [`config-example`](https://github.com/KachVev/Config-Lab/tree/main/config-example) module to see complete usage with validation, comments, and nested blocks.

---

## Define a Configuration Model

```kotlin
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
@CommentBlock("Database connection settings")
data class DatabaseConfig(
    @Comment("URL to connect to DB", position = Position.ABOVE)
    val url: String = "localhost",

    @Comment("Use secure connection", position = Position.INLINE)
    val useSSL: Boolean = true
)

@Serializable
@CommentBlock("This section defines how logging works")
data class LoggingConfig(
    @Comment("Enable logging", position = Position.INLINE)
    val enabled: Boolean = true,

    @Comment("Log level", position = Position.ABOVE)
    val level: String = "INFO"
)
```

---

### Resulting YAML Output

```yaml
# Main server configuration block
maxPlayers: 20 # Maximum number of players
# Access password (numeric)
password: 1
# Database config (field-level comment block)
database:
  # URL to connect to DB
  url: localhost
  useSSL: true # Use secure connection
# This section defines how logging works
logging:
  enabled: true # Enable logging
  # Log level
  level: INFO
```

---

## Load & Use Config in Your App

```kotlin
suspend fun main() {
    val source = FileConfigSource(Paths.get("config"))
    val format = YamlConfigFormat()
    val manager = ConfigManager(format, source)

    // Save default configuration
    manager.create("server.yml", ServerConfig())

    // Load config
    val cfg = manager.load<ServerConfig>("server")

    println("Max players = ${cfg.maxPlayers}")        // 20
    println("Password (unsafe) = ${cfg.password}")    // ***

    withSafe {
        println("Password (safe) = ${cfg.password}")  // 1
    }
}
```

---

## 🧩 Supported Annotations

| Annotation                   | Target              | Description                                                                                         |
|------------------------------|---------------------|-----------------------------------------------------------------------------------------------------|
| `@Comment("text", position)` | `PROPERTY`          | Adds a comment to a YAML field. `position = INLINE` adds to the same line, `ABOVE` places it above. |
| `@CommentBlock("text")`      | `CLASS`, `PROPERTY` | Adds a block comment above an entire YAML section (nested `data class`).                            |
| `@Required`                  | `PROPERTY`          | Ensures the value is present when loading config. Throws error if missing.                          |
| `@Range(min, max)`           | `PROPERTY`          | Validates that numeric values are within the given range.                                           |
| `@Alias("...")`              | `PROPERTY`          | Allows deserialization from legacy or alternative field names.                                      |
| `@Sensitive`                 | `PROPERTY`          | Masks the field’s value when printed (`toString()`) or logged.                                      |
| `withSafe { ... }`           | Code block          | Temporarily reveals sensitive values inside the block.                                              |

---

## Validation & Safety

```kotlin
@Serializable
data class SecureConfig(
    @Required
    val id: String,

    @Sensitive
    val token: String
)

suspend fun main() {
    val cfg = manager.load<SecureConfig>("secure")

    println(cfg) // token = ***
    withSafe {
        println(cfg) // token = "real-token"
    }
}
```

---

## License

This project is licensed under the [MIT License](https://github.com/KachVev/Config-Lab/blob/main/LICENSE).
