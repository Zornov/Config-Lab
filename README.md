# Kache Config

> **Annotation-driven, type-safe configuration for Kotlin/JVM**

[![Maven Central](https://img.shields.io/maven-central/v/dev.kache/config-core?logo=kotlin&color=brightgreen)](https://central.sonatype.com/namespace/dev.kache)
[![CI](https://github.com/<your-org>/kache-config/actions/workflows/ci.yml/badge.svg)](https://github.com/<your-org>/kache-config/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/<your-org>/kache-config)](LICENSE)

**Kache Config** lets you declare your entire application configuration as a single `@Serializable` data-class, enrich it with simple annotations, and forget about boilerplate.  
Validation, (de)serialization, comments, secure redaction, and backward-compatible renames come for free.

---

## ✨ Key Features

| Feature | What it does |
|---------|--------------|
| **Declarative models** | Plain Kotlin `data class` + `@Serializable`. |
| **Validation** | `@Required`, `@Range`, custom validators. |
| **Aliases** | `@Alias("oldName", "legacyName")` keeps old files working after refactors. |
| **Sensitive redaction** | `@Sensitive` hides secrets outside of `withSafe { }` blocks. |
| **Rich comments** | `@Comment`, `@CommentBlock` are persisted to the file. |
| **Format-agnostic** | Plug‑in HOCON / JSON / YAML / … at runtime. |
| **Multiple sources** | File, memory, classpath, S3 — implement one interface. |
| **Coroutines first** | All IO is `suspend`. |

---

## 🚀 Quick Start

### 1. Add the dependencies

<details>
<summary><strong>Gradle Kotlin DSL</strong></summary>

```kotlin
plugins {
    kotlin("jvm") version "2.0.0"
    id("dev.kache.config.sensitive") version "1.8-SNAPSHOT"   // <-- enables @Sensitive
}

repositories { mavenCentral() }

dependencies {
    implementation("dev.kache:config-core:1.8-SNAPSHOT")
    implementation("dev.kache:config-format-hocon:1.8-SNAPSHOT") // any format you like
}
```
</details>

### 2. Declare your config

```kotlin
@Serializable
@CommentBlock("⚙️  Server settings")
data class ServerConfig(

    @Required
    @Range(min = 1.0, max = 100.0)
    @Alias("oldMaxPlayers", "legacyMax")
    @Comment("Maximum number of players")
    val maxPlayers: Int = 20,

    @Sensitive
    @Comment("Admin password")
    val adminPassword: String = "change-me"
)
```

### 3. Load, use, save

```kotlin
fun main() = runBlocking {
    val manager = ConfigManager(
        HoconConfigFormat(),
        FileSource(Paths.get("server.conf"))
    )

    // Creates file with defaults if absent
    val cfg: ServerConfig = manager.load("server.conf", ServerConfig())

    println("Players allowed: ${cfg.maxPlayers}")  // safe to print

    withSafe {                                      // secrets only inside this
        println("Admin password = ${cfg.adminPassword}")
    }

    manager.save("server.conf", cfg.copy(maxPlayers = 50))
}
```

Resulting **server.conf**:

```hocon
# ⚙️  Server settings
# Maximum number of players
maxPlayers = 50

# Admin password
adminPassword = "change-me"
```

---

## 🛡️ Validation & Safety

* **Validation** — happens automatically on `load/save`; any violation throws `IllegalStateException`.
* **Sensitive fields** — are replaced with `***` in `toString`, logs and exceptions, unless the code is executed inside `withSafe { … }`.  
  The Gradle plugin adds a compile‑time check to prevent accidental leaks.

---

## 🧪 Running Tests

```bash
./gradlew test
```

See `src/test/kotlin` for examples covering encoding, validation failures, and fallback loading.

---

## 📅 Roadmap

* ✅ JSON & HOCON formats  
* 🔄 YAML & TOML modules  
* 🔒 Secrets keystore integration  
* 🔧 KSP‑based code‑gen to remove reflection at runtime  

---

## 🤝 Contributing

PRs and issues are welcome! Please open a discussion first if you plan a large change.

```bash
git clone https://github.com/<your-org>/kache-config
./gradlew build
```

* Follow the Kotlin Coding Conventions.  
* Add tests for every new feature or bugfix.  
* Run `./gradlew spotlessApply` before committing.

---

## 📜 License

Distributed under the **MIT License**. See [`LICENSE`](LICENSE) for details.

---

### 📣 Stay in touch

* **Discord:** `kache.dev/discord`  
* **Twitter/X:** `@kache_dev`  
* **Maven Central:** `dev.kache:config-*`

Happy configuring! 🎉
