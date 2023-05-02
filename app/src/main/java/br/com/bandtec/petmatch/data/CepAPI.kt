package br.com.bandtec.petmatch.data

import br.com.bandtec.petmatch.data.model.Cep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CepAPI {
    companion object{
        const val BASE_URL_CEP: String = "http://viacep.com.br/"
    }

    @GET("ws/{cep}/json/")
    fun findCep(@Path("cep") cep: String?): Call<Cep>
}