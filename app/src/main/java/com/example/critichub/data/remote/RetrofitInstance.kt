package com.example.critichub.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * سازندهٔ Retrofit برای اتصال آینده به API واقعی.
 *
 * تا زمانی که آدرس سرور مشخص نشده، هیچ baseUrl جعلی و ثابتی در برنامه قرار
 * نمی‌گیرد؛ این تابع آماده است تا با آدرس واقعی صدا زده شود.
 */
object RetrofitInstance {

    fun create(baseUrl: String): MovieApi =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
}
