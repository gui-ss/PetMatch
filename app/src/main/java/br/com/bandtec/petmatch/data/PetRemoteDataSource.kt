package br.com.bandtec.petmatch.data

import android.net.Network
import br.com.bandtec.petmatch.data.model.Filter
import br.com.bandtec.petmatch.data.model.Pet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetRemoteDataSource {

    interface PetCallback{
        fun onSuccess(response: List<Pet?>)

        fun onError(message: String?)

        fun onComplete()

        fun onSuccessRegister(response: Pet?)

        fun onErrorRegister(code: Int?)
    }

    fun findPets(callback: PetCallback){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .listPets()
            .enqueue(object : Callback<List<Pet>>{
                override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                    callback.onError(t.message)
                    callback.onComplete()
                }

                override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                    if (response.isSuccessful){
                        callback.onSuccess(response.body()!!)
                    }
                }
            })
    }

    fun addPet(callback: PetCallback, pet: Pet){
        NetworkUtils.retrofit().create(Endpoint::class.java)
            .addPet(pet)
            .enqueue(object : Callback<Pet>{
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.code() == 201 || response.code() == 200){
                        callback.onSuccessRegister(response.body())
                        callback.onComplete()
                    }else{
                        callback.onErrorRegister(response.code())
                        callback.onComplete()
                    }
                }

                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    callback.onErrorRegister(200)
                    callback.onComplete()
                }

            })
    }
}