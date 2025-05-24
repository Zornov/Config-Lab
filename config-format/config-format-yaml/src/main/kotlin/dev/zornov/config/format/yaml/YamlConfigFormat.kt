package dev.zornov.config.format.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.encodeToStream
import dev.zornov.config.annotations.Comment
import dev.zornov.config.annotations.CommentBlock
import dev.zornov.config.annotations.Position
import dev.zornov.config.core.ConfigFormat
import kotlinx.serialization.KSerializer
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class YamlConfigFormat : ConfigFormat {
    val yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = true))

    override val prefix: String = "#"
    override val fileExtension: String = "yml"

    override fun <T : Any> encode(data: T, serializer: KSerializer<T>): String {
        val output = ByteArrayOutputStream()
        yaml.encodeToStream(serializer, data, output)
        val rawYaml = output.toString(StandardCharsets.UTF_8)

        val result = StringBuilder()

        val lines = rawYaml.lines()

        fun processBlock(
            lines: List<String>,
            indentLevel: Int,
            instance: Any,
            kClass: KClass<*>
        ): List<String> {
            val result = mutableListOf<String>()
            val indent = " ".repeat(indentLevel)
            val props = kClass.memberProperties.associateBy { it.name }

            var i = 0
            while (i < lines.size) {
                val line = lines[i]
                val trimmed = line.trimStart()
                val currentIndent = line.length - trimmed.length

                if (!trimmed.contains(":")) {
                    result.add(line)
                    i++
                    continue
                }

                val key = trimmed.substringBefore(":").trim()
                val prop = props[key]

                val value = try {
                    prop?.getter?.call(instance)
                } catch (_: Exception) {
                    null
                }

                val blockFromProp = prop?.findAnnotation<CommentBlock>()?.text
                val blockFromType = (prop?.returnType?.classifier as? KClass<*>)?.findAnnotation<CommentBlock>()?.text
                val blockComment = blockFromProp ?: blockFromType
                if (!blockComment.isNullOrBlank()) {
                    result.add("$indent$prefix $blockComment")
                }

                prop?.findAnnotation<Comment>()?.takeIf { it.position == Position.ABOVE }?.let {
                    result.add("$indent$prefix ${it.text}")
                }

                val inlineComment = prop?.findAnnotation<Comment>()?.takeIf { it.position == Position.INLINE }?.text

                val nextLine = lines.getOrNull(i + 1)
                val isBlock = nextLine != null && (nextLine.length - nextLine.trimStart().length) > currentIndent

                if (isBlock && value != null) {
                    result.add(line)
                    val subBlock = mutableListOf<String>()
                    var j = i + 1
                    while (j < lines.size) {
                        val subLine = lines[j]
                        val subIndent = subLine.length - subLine.trimStart().length
                        if (subIndent <= currentIndent) break
                        subBlock.add(subLine)
                        j++
                    }

                    val nested = processBlock(subBlock, indentLevel + 2, value, value::class)
                    result.addAll(nested)
                    i = j
                } else {
                    if (inlineComment != null) {
                        result.add("$line $prefix $inlineComment")
                    } else {
                        result.add(line)
                    }
                    i++
                }
            }

            return result
        }

        data::class.findAnnotation<CommentBlock>()?.text?.let {
            result.appendLine("$prefix $it")
        }

        result.appendLine(
            processBlock(lines, 0, data, data::class)
                .joinToString("\n")
        )

        return result.toString()
    }

    override fun <T : Any> decode(text: String, serializer: KSerializer<T>): T {
        return yaml.decodeFromString(serializer, text)
    }
}
