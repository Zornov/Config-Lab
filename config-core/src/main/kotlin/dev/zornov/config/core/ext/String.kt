package dev.zornov.config.core.ext

import dev.zornov.config.core.ConfigFormat

fun String.withExt(format: ConfigFormat): String {
    return if (contains('.')) this else "$this.${format.fileExtension}"
}