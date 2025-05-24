package dev.kache.config.annotations.sensetive

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ThreadContextElement
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

val flag = ThreadLocal.withInitial { false }

class SafeElement(
    val enabled: Boolean
) : ThreadContextElement<Boolean>,
    AbstractCoroutineContextElement(Key) {

    companion object Key : CoroutineContext.Key<SafeElement>

    override fun updateThreadContext(context: CoroutineContext): Boolean =
        flag.get().also { flag.set(enabled) }

    override fun restoreThreadContext(context: CoroutineContext, oldState: Boolean) {
        flag.set(oldState)
    }
}

fun isSafe() = flag.get()

suspend inline fun <R> withSafe(
    noinline block: suspend CoroutineScope.() -> R
): R = withContext(SafeElement(true), block)