package dev.zornov.config.annotations.sensetive

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable(with = ProtectedStringSerializer::class)
data class Protected<T>(val value: T) : Number() {

    inline fun <N> reveal(real: () -> N, fake: () -> N): N =
        if (isSafe() && value is Number) real() else fake()

    override fun toInt()    = reveal({ (value as Number).toInt()  }, Random::nextInt)
    override fun toLong()   = reveal({ (value as Number).toLong() }, Random::nextLong)
    override fun toFloat()  = reveal({ (value as Number).toFloat() }) { Float.fromBits(Random.nextInt()) }
    override fun toDouble() = reveal({ (value as Number).toDouble() }) { Double.fromBits(Random.nextLong()) }
    override fun toByte()   = reveal({ (value as Number).toByte() })   { Random.nextInt().toByte() }
    override fun toShort()  = reveal({ (value as Number).toShort() })  { Random.nextInt().toShort() }

    override fun toString() = if (isSafe()) value.toString() else "***"
}