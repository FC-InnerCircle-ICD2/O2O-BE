package org.fastcampus.common.util

@JvmName("throwIfNullOrEmptyExt")
fun <T> Collection<T>?.throwIfNullOrEmpty(throwable: Throwable): Collection<T> {
    if (isNullOrEmpty()) {
        throw throwable
    }
    return this
}

fun <T> throwIfNullOrEmpty(collection: Collection<T>?, throwable: Throwable): Collection<T> {
    if (collection.isNullOrEmpty()) {
        throw throwable
    }
    return collection
}
