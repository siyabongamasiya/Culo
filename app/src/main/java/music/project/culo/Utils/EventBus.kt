package music.project.culo.Utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private var _state = MutableSharedFlow<String>()
    val state = _state.asSharedFlow()

    suspend fun updateState(currentState : String){
        _state.emit(currentState)
    }
}