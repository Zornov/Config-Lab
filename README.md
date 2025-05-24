# Kache Config

> **Annotation-driven, type-safe configuration for Kotlin/JVM**

[![config-annotations](https://img.shields.io/maven-metadata/v/github/KachVev/Config-Lab/dev.kache.config.annotations/config-annotations/maven-metadata.xml)](https://github.com/KachVev/Config-Lab/packages?package_type=maven)
[![config-core](https://img.shields.io/maven-metadata/v/github/KachVev/Config-Lab/dev.kache.config.sensitive/config-core/maven-metadata.xml)](https://github.com/KachVev/Config-Lab/packages?package_type=maven)
[![config-yml](https://img.shields.io/maven-metadata/v/github/KachVev/Config-Lab/dev.kache.config.sensitive/config-yml/maven-metadata.xml)](https://github.com/KachVev/Config-Lab/packages?package_type=maven)
[![sensitive-plugin](https://img.shields.io/maven-metadata/v/github/KachVev/Config-Lab/dev.kache.config.sensitive/sensitive-gradle-plugin/maven-metadata.xml)](https://github.com/KachVev/Config-Lab/packages?package_type=maven)
[![kotlin-plugin](https://img.shields.io/maven-metadata/v/github/KachVev/Config-Lab/dev.kache.config.sensitive/kotlin-plugin/maven-metadata.xml)](https://github.com/KachVev/Config-Lab/packages?package_type=maven)

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

