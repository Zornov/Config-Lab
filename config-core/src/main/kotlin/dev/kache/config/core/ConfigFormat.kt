package dev.kache.config.core

import kotlinx.serialization.KSerializer

interface ConfigFormat {
    val prefix: String
    val suffix: String
        get() = ""

    val fileExtension: String

    fun <T : Any> encode(data: T, serializer: KSerializer<T>): String
    fun <T : Any> decode(text: String, serializer: KSerializer<T>): T
}