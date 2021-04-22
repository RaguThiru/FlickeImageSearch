package com.ragu.flickerimagesearch.repository

import com.ragu.flickerimagesearch.service.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun searchPhoto(search:String,page:Int)=apiHelper.searchPhoto(search,page)
}