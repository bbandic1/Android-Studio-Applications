package com.example.myproject

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface TrefleService {
    @GET("plants/search")
    suspend fun dohvatiBiljke(
        @Query("q") latinIme: String,
        @Query("token") apiKey: String
    ): BiljkaResponse

    @GET("plants/{id}")
    suspend fun dohvatiDetaljeBiljke(
        @Path("id") id: Int?,
        @Query("token") apiKey: String
    ): BiljkaDetaljiResponse

    @GET("plants")
    suspend fun dohvatiBiljkeSaBojom(
        @Query("filter{flower_color}") bojaBiljke: String,
        @Query("token") apiKey: String,
        @Query("page") page: Int
    ): BiljkaResponse
    object RetrofitClient {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url()
                //Log.d("TrefleDAO", "Otvoreni URL: $url")
                chain.proceed(request)
            }
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://trefle.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val trefleService: TrefleService by lazy {
            retrofit.create(TrefleService::class.java)
        }

    }
}

