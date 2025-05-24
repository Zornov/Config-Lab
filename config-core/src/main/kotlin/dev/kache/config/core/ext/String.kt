package dev.kache.config.core.ext

import dev.kache.config.core.ConfigFormat

fun String.withExt(format: ConfigFormat): String {
    return if (contains('.')) this else "$this.${format.fileExtension}"
}