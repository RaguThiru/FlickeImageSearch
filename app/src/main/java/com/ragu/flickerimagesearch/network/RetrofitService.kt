package com.ragu.flickerimagesearch.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.ragu.flickerimagesearch.service.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitService {
    private const val BASE_URL = "https://api.flickr.com/services/"

    fun okHttpClient(context: Context): OkHttpClient {

        val cacheSize = (10 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()

        return okHttpClient
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    private val retrofit = fun(context: Context) = Retrofit.Builder()
        .baseUrl(BASE_URL).client(okHttpClient(context))
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: (Context) -> ApiService =
        fun(context: Context) = retrofit(context).create(ApiService::class.java)
}