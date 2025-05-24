package dev.zornov.config.core.validator

import dev.zornov.config.annotations.Range
import dev.zornov.config.annotations.Required
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class DefaultConfigValidator : ConfigValidator {

    override fun validate(instance: Any) {
        val kClass: KClass<out Any> = instance::class

        for (prop in kClass.memberProperties) {
            val value = prop.getter.call(instance)

            if (prop.findAnnotation<Required>() != null) {
                if (value == null || isEmpty(value)) {
                    error("Field '${prop.name}' is required and must not be null or empty.")
                }
            }

            val range = prop.findAnnotation<Range>()
            if (range != null && value is Number) {
                val number = value.toDouble()
                if (number < range.min || number > range.max) {
                    error("Field '${prop.name}' must be in range ${range.min}..${range.max}, but was $number")
                }
            }
        }
    }

    fun isEmpty(value: Any): Boolean {
        return when (value) {
            is String -> value.isEmpty()
            is Collection<*> -> value.isEmpty()
            is Map<*, *> -> value.isEmpty()
            is Array<*> -> value.isEmpty()
            else -> false
        }
    }
}
