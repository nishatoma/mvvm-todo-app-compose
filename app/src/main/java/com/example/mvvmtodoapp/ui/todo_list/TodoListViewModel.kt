package com.example.mvvmtodoapp.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodoapp.data.Todo
import com.example.mvvmtodoapp.data.TodoRepository
import com.example.mvvmtodoapp.util.Routes
import com.example.mvvmtodoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Since we want to inject the repository
// using dagger hilt, we do this in the constructor,
// using @Inject annotation
@HiltViewModel
class TodoListViewModel @Inject constructor(
    // As soon as we annotate inject constructor or property
    // with inject, that will tell dagger hilt please look at your module
    // if there is any dependency of this type (TodoRepository)
    // that you know how to create!
    private val repository: TodoRepository
): ViewModel() {
    // all our business logic and state variables
    val todos = repository.getTodos()

    // The to do that we deleted, incase we wanna undo
    private var deletedTodo: Todo? = null

    // we need a channel or shared flow
    // a channel is used if we want to send one time events
    private val _uiEvent = Channel<UiEvent>()
    // represent this channel as a flow
    val uiEvent = _uiEvent.receiveAsFlow()

    // this needs to be in a coroutine
    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoListEvent.OnAddTodoClick -> {
                // Send navigate event to our UI
                // that we want to navigate to add edit to do list screen
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let {
                    todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }

            is TodoListEvent.OnDeleteToDoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackbar(
                        "Todo deleted",
                        "Undo"
                    ))
                }
            }

            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    // We need a coroutine to run our db functions
                    repository.insertTodo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            // here we can run our suspend functions
            _uiEvent.send(event)
        }
    }
}