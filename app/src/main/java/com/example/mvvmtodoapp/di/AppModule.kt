package com.example.mvvmtodoapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmtodoapp.data.TodoDatabase
import com.example.mvvmtodoapp.data.TodoRepository
import com.example.mvvmtodoapp.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Why called AppModule?
// defines the dependencies we need in our package,
// and their lifetimes
@Module
// defines how long our dependencies are going to live
// since this app module only contains singletons
@InstallIn(SingletonComponent::class)
object AppModule {
    // define how the dependencies should be created
    // 'provies' dependency, and is singleton
    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(app,
        TodoDatabase::class.java,
        "todo_db").build()
    }

    // Now we need to provide the repository
    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        // This is how we get a repository, given our DB
        // instance
        return TodoRepositoryImpl(db.dao)
    }
}