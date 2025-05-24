package dev.kache.config.annotations.sensetive

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class ProtectedSerializer<T : Any>(
    val dataSerializer: KSerializer<T>
) : KSerializer<Protected<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Protected<T>) {
        encoder.encodeSerializableValue(dataSerializer, value.value)
    }

    override fun deserialize(decoder: Decoder): Protected<T> {
        val v = decoder.decodeSerializableValue(dataSerializer)
        return Protected(v)
    }
}