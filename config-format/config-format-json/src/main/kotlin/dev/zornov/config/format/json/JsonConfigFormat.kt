package dev.zornov.config.format.json

import dev.zornov.config.annotations.Comment
import dev.zornov.config.annotations.CommentBlock
import dev.zornov.config.annotations.Position
import dev.zornov.config.core.ConfigFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class JsonConfigFormat : ConfigFormat {
    val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    override val prefix: String = "//"
    override val fileExtension: String = "json"

    override fun <T : Any> encode(data: T, serializer: KSerializer<T>): String {
        val rawJson = json.encodeToString(serializer, data)
        val result = StringBuilder()

        val lines = rawJson.lines()
        val props = data::class.memberProperties.associateBy { it.name }

        data::class.findAnnotation<CommentBlock>()?.text?.let {
            result.appendLine("$prefix $it")
        }

        for (line in lines) {
            val trimmed = line.trimStart()
            val key = trimmed
                .takeIf { it.contains(":") }
                ?.substringBefore(":")
                ?.trim('"')

            val prop = props[key]

            val blockFromProp = prop?.findAnnotation<CommentBlock>()?.text
            val blockFromType = (prop?.returnType?.classifier as? KClass<*>)?.findAnnotation<CommentBlock>()?.text
            val blockComment = blockFromProp ?: blockFromType
            if (!blockComment.isNullOrBlank()) {
                result.appendLine("$prefix $blockComment")
            }

            prop?.findAnnotation<Comment>()?.takeIf { it.position == Position.ABOVE }?.let {
                result.appendLine("$prefix ${it.text}")
            }

            if (prop != null && prop.findAnnotation<Comment>()?.position == Position.INLINE) {
                val inline = prop.findAnnotation<Comment>()!!.text
                result.appendLine("$line $prefix $inline")
            } else {
                result.appendLine(line)
            }
        }

        return result.toString()
    }

    override fun <T : Any> decode(text: String, serializer: KSerializer<T>): T {
        val jsonText = text.lines().joinToString("\n") { line ->
            val index = line.indexOf(prefix)
            if (index >= 0) line.take(index).trimEnd() else line
        }

        return json.decodeFromString(serializer, jsonText)
    }
}
