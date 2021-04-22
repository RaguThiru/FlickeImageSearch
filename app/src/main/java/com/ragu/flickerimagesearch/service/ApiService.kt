package com.ragu.flickerimagesearch.service

import com.ragu.flickerimagesearch.model.ResponseJson
import com.ragu.flickerimagesearch.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //String method = "flickr.photos.getRecent";
   // val API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736"


    @GET("rest/")
    suspend fun searchImages(@Query("method") method:String="flickr.photos.search", @Query("api_key")api_key:String=Constant.APIKEY,@Query("format")format:String="json", @Query("text") text:String,@Query("nojsoncallback")nojsoncallback:Int=1,@Query("page")page:Int=1): ResponseJson

}