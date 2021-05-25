package com.squad.tdd.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

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