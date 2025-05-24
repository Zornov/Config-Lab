package dev.zornov.config.core

import kotlinx.serialization.KSerializer

class FakeConfigFormat : ConfigFormat {
    override val prefix: String = "/*"
    override val suffix: String = "*/"

    override val fileExtension: String = ".fake"

    override fun <T : Any> encode(data: T, serializer: KSerializer<T>): String {
        return data.toString()
    }

    override fun <T : Any> decode(text: String, serializer: KSerializer<T>): T {
        return serializer.deserialize(FakeDecoder(text))
    }
}