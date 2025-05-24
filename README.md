# ConfigLab

[![GitHub stars](https://img.shields.io/github/stars/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/stargazers)
[![License](https://img.shields.io/github/license/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/blob/main/LICENSE)
[![Contributors](https://img.shields.io/github/contributors/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/graphs/contributors)

> Annotation-driven, configuration library for Kotlin

---

## Features

| Feature                 | Description                                                                                              |
|-------------------------|----------------------------------------------------------------------------------------------------------|
| **Declarative models**  | Plain Kotlin `data class` + `@Serializable`.                                                             |
| **Validation**          | Built-in validation via `@Required`, `@Range`, or custom annotations.                                    |
| **Sensitive redaction** | Redacts `@Sensitive` fields unless explicitly revealed inside `withSafe {}` Work only with gradle plugin |
| **Rich comments**       | Support for inline and block YAML comments via `@Comment`                                                |
| **Pluggable formats**   | YAML support via module; other formats can be plugged in.                                                |
| **Multiple sources**    | Load from files or any custom source implementation.                                                     |

---

## Quickstart

### Add Dependencies

<details>
<summary><strong>Gradle (Kotlin DSL)</strong></summary>

```kotlin
plugins {
    // Enable @Sensitive support
    id("dev.kache.config.sensitive") version "1.0" 
}

dependencies {
    // Core runtime
    implementation("dev.kache.config:core:1.0")

    // Annotations
    implementation("dev.kache.config:annotations:1.0")

    // YAML formatter
    implementation("dev.kache.config.format:yml:1.0")

}
```

</details>

---

### Full example can be found in the [`config-example`](https://github.com/KachVev/Config-Lab/tree/main/config-example) module

Browse the example to see how to define configs, load them, and safely use sensitive data in a Kotlin application.

---

### Define a Configuration Model

```kotlin
@Serializable
data class TestConfig(
    @Required
    @Range(min = 1.0, max = 100.0)
    @Comment("Maximum number of players")
    val maxPlayers: Int = 20,

    @Sensitive
    @Comment("Access password")
    val password: String = "secret"
)
```

---

### Load & Use Config in Your App

```kotlin
suspend fun main() {
    val source = FileConfigSource("Path to configurations")
    val format = YamlConfigFormat()
    val manager = ConfigManager(format, source)

    // Save default configuration
    manager.save("config.yml", TestConfig())

    // Load config
    val cfg = manager.load<TestConfig>("config.yml")

    println("Max players = ${cfg.maxPlayers}")        // Will output 20
    println("Password (unsafe) = ${cfg.password}")    // Will output ***
    
    withSafe {
        println("Password (safe) = ${cfg.password}")  // Will output "secret"
    }
}
```
---