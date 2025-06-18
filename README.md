# ConfigLab

[![License](https://img.shields.io/badge/license-CC--BY--NC%204.0-blue)](https://creativecommons.org/licenses/by-nc/4.0/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.21-blue)](https://kotlinlang.org/)

> Annotation-driven, configuration library for Kotlin

---

## Features

| Feature                 | Description                                                                                                 |
|-------------------------|-------------------------------------------------------------------------------------------------------------|
| **Validation**          | Built-in validation via `@Required`, `@Range`.                                                              |
| **Sensitive redaction** | Redacts `@Sensitive` fields unless explicitly revealed inside `withSafe {}`. Works only with Gradle plugin. |
| **Rich comments**       | Support for inline (`@Comment(..., INLINE)`) and block-level (`@CommentBlock`) YAML comments.               |
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
    
    // Or you can use Json formatter
    // implementation("dev.zornov.config.format:json:1.0")
}
````

</details>

---

## Full example

Browse the [`config-example`](https://github.com/Zornoov/Config-Lab/tree/main/config-example) module to see complete usage with validation, comments, and nested blocks.

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

    // You can use either YAML or JSON
    val yaml = ConfigManager(YamlConfigFormat(), source)
    val json = ConfigManager(JsonConfigFormat(), source)

    // Save defaults (only needed once)
    yaml.create("server.yml", ServerConfig()) // Or simply : yaml.create("server", ServerConfig())
    json.create("server.json", ServerConfig()) // Or simply : json.create("server", ServerConfig())

    // Load YAML
    val cfgYml = yaml.load<ServerConfig>("server")
    println("[YAML] Max players = ${cfgYml.maxPlayers}")  // 20

    // Load JSON
    val cfgJson = json.load<ServerConfig>("server")
    println("[JSON] Max players = ${cfgJson.maxPlayers}") // 20

    // Sensitive printing
    withSafe {
        println("[YAML] Password = ${cfgYml.password}")
        println("[JSON] Password = ${cfgJson.password}")
    }
}

```

---

## Supported Annotations

| Annotation                   | Target              | Description                                                                                         |
|------------------------------|---------------------|-----------------------------------------------------------------------------------------------------|
| `@Comment("text", position)` | `PROPERTY`          | Adds a comment to a YAML field. `position = INLINE` adds to the same line, `ABOVE` places it above. |
| `@CommentBlock("text")`      | `CLASS`, `PROPERTY` | Adds a block comment above an entire YAML section (nested `data class`).                            |
| `@Required`                  | `PROPERTY`          | Ensures the value is present when loading config. Throws error if missing.                          |
| `@Range(min, max)`           | `PROPERTY`          | Validates that numeric values are within the given range.                                           |
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

This project is licensed under the [Creative Commons Attribution-NonCommercial 4.0 International License](https://creativecommons.org/licenses/by-nc/4.0/).

Copyright © 2025
Sasha Zorov
