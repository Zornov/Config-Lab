# Kache Config

> **Annotation-driven, type-safe configuration for Kotlin/JVM**

[![config-annotations](https://jitpack.io/v/com.github.KachVev.Config-Lab/config-annotations.svg)](https://jitpack.io/#com.github.KachVev/Config-Lab)
[![config-core](https://jitpack.io/v/com.github.KachVev.Config-Lab/config-core.svg)](https://jitpack.io/#com.github.KachVev/Config-Lab)
[![config-yml](https://jitpack.io/v/com.github.KachVev.Config-Lab/config-yml.svg)](https://jitpack.io/#com.github.KachVev/Config-Lab)
[![sensitive-plugin](https://jitpack.io/v/com.github.KachVev.Config-Lab/sensitive-gradle-plugin.svg)](https://jitpack.io/#com.github.KachVev/Config-Lab)
[![kotlin-plugin](https://jitpack.io/v/com.github.KachVev.Config-Lab/kotlin-plugin.svg)](https://jitpack.io/#com.github.KachVev/Config-Lab)

[![License](https://img.shields.io/github/license/KachVev/Config-Lab)](LICENSE)

**Kache Config** lets you declare your entire application configuration as a single `@Serializable` data-class, enrich it with simple annotations, and forget about boilerplate.  
Validation, (de)serialization, comments, secure redaction, and backward-compatible renames come for free.

---

## ✨ Key Features

| Feature | What it does |
|---------|--------------|
| **Declarative models** | Plain Kotlin `data class` + `@Serializable`. |
| **Validation** | `@Required`, `@Range`, custom validators. |
| **Sensitive redaction** | `@Sensitive` hides secrets outside of `withSafe { }` blocks. |
| **Rich comments** | `@Comment` are persisted to the file. |
| **Format-agnostic** | YAML |
| **Multiple sources** | File, other — implement one interface. |

