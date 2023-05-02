package br.com.bandtec.petmatch.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkUtils {

        companion object{

            var okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            
            @JvmStatic
            fun retrofit(): Retrofit{
                return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Endpoint.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            @JvmStatic
            fun retrofitCep(): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(CepAPI.BASE_URL_CEP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
    }
