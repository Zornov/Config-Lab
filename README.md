# Kache Config

> **Annotation-driven, type-safe configuration for Kotlin/JVM**

[![Maven Central](https://img.shields.io/maven-central/v/dev.kache.config.sensitive/config-core?logo=kotlin&color=brightgreen)](https://central.sonatype.com/namespace/dev.kache.config.sensitive)
[![CI](https://github.com/KachVev/Config-Lab/actions/workflows/ci.yml/badge.svg)](https://github.com/KachVev/Config-Lab/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/KachVev/Config-Lab)](https://github.com/KachVev/Config-Lab/blob/main/LICENSE)


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

