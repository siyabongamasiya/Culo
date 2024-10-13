package media.project.flow_extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * created these functions to make a clear difference between the general cold flow
 * and hot flows.a cold flow requires there to be a collector and therefore wont emit
 * until collector starts collecting so it is similar to triggering a fetch.a stateflow
 * will emit regardles if there are collectors or not but if collector subscribes they
 * will collect the current value and wait for updates and with shared flows you subscribe
 * and wait as there are no current or saved values(unless replayed)
 */

suspend fun <T> Flow<T>.fetch (block : (value : T) -> Unit) {
    this.collect{value ->
        block.invoke(value)
    }
}

suspend fun <T> StateFlow<T>.collectAndWait (block : (value : T) -> Unit) : Nothing{
    this.collect{value ->
        block.invoke(value)
    }
}

suspend fun <T> SharedFlow<T>.wait(block : (value : T) -> Unit) : Nothing{
    this.collect{value ->
        block.invoke(value)
    }
}




