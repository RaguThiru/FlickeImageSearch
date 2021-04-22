package com.ragu.flickerimagesearch

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.ragu.flickerimagesearch.network.RetrofitService

class MyApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

    }
}