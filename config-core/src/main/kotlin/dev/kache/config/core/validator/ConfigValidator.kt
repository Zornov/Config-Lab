package dev.kache.config.core.validator

interface ConfigValidator {
    fun validate(instance: Any)
}
