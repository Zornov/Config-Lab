package dev.zornov.config.annotations.sensetive

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ProtectedStringSerializer : KSerializer<Protected<String>> {
    override val descriptor = PrimitiveSerialDescriptor("Protected", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Protected<String>) {
        encoder.encodeString(value.value)
    }
    override fun deserialize(decoder: Decoder): Protected<String> {
        return Protected(decoder.decodeString())
    }
}