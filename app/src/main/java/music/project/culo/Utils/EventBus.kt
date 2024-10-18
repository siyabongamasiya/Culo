package music.project.culo.Utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object EventBus {
    private var _state = MutableStateFlow(States.INPROGRESS.toString())
    val state = _state.asStateFlow()

    fun updateState(currentState : String){
        _state.value = currentState
    }
}