package com.example.mvvmtodoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// When we wanna use dagger hilt,
// we just need an application class to inherit
// from application
@HiltAndroidApp
class TodoApp: Application()