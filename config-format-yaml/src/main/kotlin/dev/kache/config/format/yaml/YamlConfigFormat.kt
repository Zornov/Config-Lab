package dev.kache.config.format.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.encodeToStream
import dev.kache.config.annotations.Comment
import dev.kache.config.annotations.CommentBlock
import dev.kache.config.core.ConfigFormat
import kotlinx.serialization.KSerializer
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class YamlConfigFormat : ConfigFormat {
    val yaml = Yaml(
        configuration = YamlConfiguration(encodeDefaults = true)
    )

    override val prefix: String = "#"
    override val fileExtension: String = "yml"

    override fun <T : Any> encode(data: T, serializer: KSerializer<T>): String {
        val output = ByteArrayOutputStream()

        yaml.encodeToStream(serializer, data, output)
        val rawYaml = output.toString(StandardCharsets.UTF_8)

        val commented = StringBuilder()

        data::class.findAnnotation<CommentBlock>()?.text?.let {
            commented.appendLine("$prefix $it $suffix")
        }

        val propertyComments = data::class.memberProperties.associateBy(
            { it.name },
            { it.findAnnotation<Comment>()?.text }
        )

        for (line in rawYaml.lines()) {
            val key = line.substringBefore(":").trim()

            if (key in propertyComments) {
                val comment = propertyComments[key]
                if (!comment.isNullOrBlank()) {
                    commented.appendLine("$prefix $comment $suffix")
                }
            }

            commented.appendLine(line)
        }

        return commented.toString()
    }

    override fun <T : Any> decode(text: String, serializer: KSerializer<T>): T {
        return yaml.decodeFromString(serializer, text)
    }
}