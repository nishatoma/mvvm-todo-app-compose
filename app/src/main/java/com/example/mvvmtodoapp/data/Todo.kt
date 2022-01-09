package com.example.mvvmtodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// This tells room we want to have the 'To Do' object
// as a table in our database
@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null
)
