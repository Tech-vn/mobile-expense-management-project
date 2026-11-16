package com.example.chitieu

import android.app.Application
import com.example.chitieu.data.local.DatabaseSeeder
import com.example.chitieu.di.AppModule
import kotlinx.coroutines.launch

class ChiTieuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
        AppModule.ioScope.launch {
            DatabaseSeeder.seedIfFirstRun(AppModule.database)
        }
    }
}
