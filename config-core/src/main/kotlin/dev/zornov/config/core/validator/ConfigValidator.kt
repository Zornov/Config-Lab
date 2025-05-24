package dev.zornov.config.core.validator

interface ConfigValidator {
    fun validate(instance: Any)
}
