package com.squad.tdd

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class ContentTestExtension : BeforeEachCallback {

    @ExperimentalCoroutinesApi
    override fun beforeEach(context: ExtensionContext?) {
        // Set Coroutine Dispatcher.
        Dispatchers.setMain(
            context?.root
                ?.getStore(ExtensionContext.Namespace.create("extensionNamespace"))
                ?.get(
                    ExtensionContext.Namespace.create("testCoroutineScopeNamespace"),
                    TestCoroutineDispatcher::class.java
                )!!
        )

        // Set LiveData Executor.
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
            override fun postToMainThread(runnable: Runnable) = runnable.run()
            override fun isMainThread(): Boolean = true
        })
    }
}