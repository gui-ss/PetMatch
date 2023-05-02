package br.com.bandtec.petmatch.data

import br.com.bandtec.petmatch.data.model.Filter
import br.com.bandtec.petmatch.data.model.Pet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterRemoteDataSource {

    interface FilterCallback{
        fun onError(message: String?)

        fun onComplete()

        fun onSuccessFilter(response: List<Pet>?)

    }

    fun filterPet(callback: FilterCallback, petFilter: Filter){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .filterPet(petFilter)
            .enqueue(object : Callback<List<Pet>>{
                override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                    if (response.code() == 200){
                        callback.onSuccessFilter(response.body())
                        callback.onComplete()
                    }
                    callback.onComplete()
                }

                override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                    callback.onComplete()
                }

            })
    }

}