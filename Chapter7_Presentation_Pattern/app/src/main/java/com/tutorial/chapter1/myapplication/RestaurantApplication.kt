package com.tutorial.chapter1.myapplication

import android.app.Application
import android.content.Context

class RestaurantApplication: Application() {
    init { app = this }
    companion object {
        private lateinit var app: RestaurantApplication
        fun getAppContext(): Context =
            app.applicationContext
    }
}