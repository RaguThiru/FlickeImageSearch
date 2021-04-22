package com.ragu.flickerimagesearch.service

import com.ragu.flickerimagesearch.utils.Constant


class ApiHelper(private val apiService: ApiService) {
    suspend fun searchPhoto(searchText: String,page:Int) =
        apiService.searchImages(Constant.METHOD_NAME,Constant.APIKEY,Constant.FORMAT,searchText,1,page)
}