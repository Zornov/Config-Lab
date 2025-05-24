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
| **Format-agnostic** | YAML |
| **Multiple sources** | File, memory, classpath, S3 — implement one interface. |
| **Coroutines first** | All IO is `suspend`. |

