package com.squad.tdd.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

fun <T> LiveData<T>.onEach(onNext: (T) -> Unit): LiveData<T> {
    val requestMediator = MediatorLiveData<T>()
    val observerRequestSource = Observer<T> { }
    requestMediator.addSource(this) {
        requestMediator.value = it
        onNext(it)
    }
    requestMediator.observeForever(observerRequestSource)
    return requestMediator
}

// FIXME: refactor zip extension to remove the zipper auxiliary class
class Zipper(val first: Any? = null, val second: Any? = null, val third: Any? = null)

fun <T1, T2, T3, T4, R> Flow<T1>.zip(
    first: Flow<T2>,
    second: Flow<T3>,
    third: Flow<T4>,
    transform: suspend (T1, T2, T3, T4) -> R
): Flow<R> {
    return this.zip(first) { a, b -> Zipper(a, b) }
        .zip(second) { zipper, c -> Zipper(zipper.first, zipper.second, c) }
        .zip(third) { zipper, d ->
            transform(
                zipper.first as T1,
                zipper.second as T2, zipper.third as T3, d
            )
        }
}