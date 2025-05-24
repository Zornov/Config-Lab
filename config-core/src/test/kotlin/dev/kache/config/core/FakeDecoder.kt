package dev.kache.config.core

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule

class FakeDecoder(input: String) : Decoder, CompositeDecoder {
    val map: Map<String, String>
    var index = 0
    lateinit var descriptor: SerialDescriptor

    init {
        val body = input.substringAfter("(").substringBeforeLast(")")
        map = body.split(",").mapNotNull {
            val (k, v) = it.trim().split("=", limit = 2)
            k to v
        }.toMap()
    }

    override val serializersModule = EmptySerializersModule

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        this.descriptor = descriptor
        index = 0
        return this
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        return if (index < descriptor.elementsCount) index++ else CompositeDecoder.DECODE_DONE
    }

    override fun endStructure(descriptor: SerialDescriptor) {}

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = true
    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null

    override fun decodeString(): String = error("Unsupported: use structured decode")


    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        return map[descriptor.getElementName(index)] ?: ""
    }

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        return map[descriptor.getElementName(index)]?.toIntOrNull() ?: 0
    }

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double {
        return map[descriptor.getElementName(index)]?.toDoubleOrNull() ?: 0.0
    }

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float {
        return map[descriptor.getElementName(index)]?.toFloatOrNull() ?: 0f
    }

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long {
        return map[descriptor.getElementName(index)]?.toLongOrNull() ?: 0
    }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
        return map[descriptor.getElementName(index)]?.toBooleanStrictOrNull() ?: false
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
        return map[descriptor.getElementName(index)]?.firstOrNull() ?: ' '
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
        return map[descriptor.getElementName(index)]?.toShortOrNull() ?: 0
    }

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
        return map[descriptor.getElementName(index)]?.toByteOrNull() ?: 0
    }


    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = this
    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val nestedDecoder = this
        return deserializer.deserialize(nestedDecoder)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        val name = descriptor.getElementName(index)
        if (!map.containsKey(name)) return null
        return deserializer.deserialize(this)
    }

    override fun decodeBoolean(): Boolean = error("Use decodeBooleanElement")
    override fun decodeByte(): Byte = error("Use decodeByteElement")
    override fun decodeChar(): Char = error("Use decodeCharElement")
    override fun decodeDouble(): Double = error("Use decodeDoubleElement")
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = error("Use decodeEnum")
    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this
    override fun decodeFloat(): Float = error("Use decodeFloatElement")
    override fun decodeInt(): Int = error("Use decodeIntElement")
    override fun decodeLong(): Long = error("Use decodeLongElement")
    override fun decodeShort(): Short = error("Use decodeShortElement")
}