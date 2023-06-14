package com.example.courcework.app.presentation

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e("EXCEPTION", "Exception", e)
        Result.failure(e)
    }
}

fun <T> Flow<T>.catchNonCancellationAndEmit(onError: T): Flow<T> = flow {
    try {
        collect { value -> emit(value) }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e("EXCEPTION", "Exception", e)
        emit(onError)
    }
}