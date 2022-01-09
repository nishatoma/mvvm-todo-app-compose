package com.example.mvvmtodoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Data access object
// used to access our DATA
// defines the different ways to access data
@Dao
interface TodoDao {

    // define the functions we need to interact with our DB
    // like insert to do, get to dos, etc
    // on Conflict means what happens when we insert an
    // ID that already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo?

    // flows mean, we get a real time update
    // as soon as our DB changes
    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>
}