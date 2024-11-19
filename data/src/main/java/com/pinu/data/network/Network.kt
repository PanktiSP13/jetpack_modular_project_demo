package com.pinu.data.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class Network private constructor() {

    private var baseUrl: String = "https://bookhub.free.beeceptor.com/"
    private val customHeaders = mutableMapOf<String, String>()

    companion object {

        private var instance: Network? = null

        fun init(): Network {
            if (instance == null) {
                instance = Network()
            }
            return instance!!
        }
    }

    fun setBaseUrl(url: String): Network {
        // replace default base url with custom base url
        baseUrl = url
        return this
    }

    fun setCustomHeader(key: String, value: String) {
        customHeaders[key] = value
    }

    fun setCustomHeaders(headerList: Map<String, String>) {
        // add additional headers
        customHeaders.putAll(headerList)
    }

    fun prepareFilePart(
        partName: String,
        mimeType: String = "image/*",
        file: File
    ): MultipartBody.Part {
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()
    }

    private fun httpClient(): OkHttpClient {

        // to enable logging
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(Interceptor { chain ->
                val request: Request.Builder = chain.request().newBuilder()

                // add custom header
                customHeaders.forEach { (key, value) ->
                    request.addHeader(key, value)
                }

                //default header
                request.addHeader("Content-Type", "application/json")
                chain.proceed(request.build())
            }).build()
    }


    // API service
    val apiService: NetworkAPIs by lazy { retrofit().create(NetworkAPIs::class.java) }

}