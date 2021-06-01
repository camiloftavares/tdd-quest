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

fun <T1, T2, T3, T4, R> Flow<T1>.zip(
    second: Flow<T2>,
    third: Flow<T3>,
    fourth: Flow<T4>,
    transform: suspend (T1, T2, T3, T4) -> R
): Flow<R> =
    this.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c -> Triple(a, b, c) }
        .zip(fourth) { triple, d -> transform(triple.first, triple.second, triple.third, d) }