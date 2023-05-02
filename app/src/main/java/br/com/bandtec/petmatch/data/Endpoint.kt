package br.com.bandtec.petmatch.data

import br.com.bandtec.petmatch.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface Endpoint {

    companion object{
         const val BASE_URL: String =  "http://52.3.6.109:8080/app/"
         const val BASE_URL_MOCK: String =  "https://60a29dd27c6e8b0017e25d6c.mockapi.io/"
    }

    @GET("users")
    fun list(): Call<List<User>>

    @GET("users/{id}")
    fun getUserMock(
        @Path("id") id: String?
    ) : Call<User>

    @GET("usuarios/{id}")
    fun getUser(
       @Path("id") id: String
    ) : Call<User>

    @GET("usuarios/{email}/{senha}")
    fun userLogin(
        @Path("email") email: String,
        @Path("senha") senha: String
    ) : Call<User>

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    fun addUser(
        @Body user: User
    ): Call<User>

    @GET("pets")
    fun listPets(): Call<List<Pet>>

    @GET("pets/{id}")
    fun getPetById(
        @Path("id") id: String?
    ) : Call<Pet>

    @Headers("Content-Type: application/json")
    @POST("pets")
    fun addPet(
        @Body pet: Pet
    ): Call<Pet>

    @Headers("Content-Type: application/json")
    @POST("pets/filtrar")
    fun filterPet(
        @Body petFilter: Filter
    ): Call<List<Pet>>

    @GET("pets/meus-pets")
    fun listMyPets(): Call<List<Pet>>

    @GET("usuarios/listar-pets-curtidos-por-mim")
    fun listPetsLiked(): Call<List<Match>>

    @GET("usuarios/listar-pets-curtidos")
    fun listMyPetsLiked(): Call<List<Match>>

    @GET("usuarios/listar-match")
    fun listMatchs(): Call<List<Match>>

    @Headers("Content-Type: application/json")
    @PATCH("pets")
    fun alterPet(
        @Body pet: NewPet
    ): Call<NewPet>

    @Headers("Content-Type: application/json")
    @PATCH("usuarios")
    fun alterUser(
        @Body user: User
    ): Call<User>

    @PATCH("usuarios/gostarAdotante/{idAdotante}/{idPet}")
    fun darMatch(
        @Path("idAdotante") idAdotante: Int,
        @Path("idPet") idPet: Int
    ): Call<Match>

    @Headers("Content-Type: application/json")
    @POST("usuarios/gostarPet")
    fun darLike(
        @Body dataLike: Like
    ): Call<Like>

    @DELETE("pets/{id}")
    fun deletePetBy(
        @Path("id") id: String?
    ) : Call<Pet>

    @DELETE("usuarios/gostarAdotante/{idAdotante}/{idPet}/false")
    fun recusarMatch(
        @Path("idAdotante") idAdotante: Int,
        @Path("idPet") idPet: Int
    ) : Call<Void>

    @Headers("Content-Type: application/json")
    @POST("vacinas")
    fun addVaccine(
        @Body vacina: Vacina
    ): Call<Void>

    @GET("pets/ultimo-id")
    fun searchPetLastId(): Call<Int>

    @GET("vacinas/{idPet}")
    fun listarVacinas(
        @Path("idPet") id: Int
    ): Call<List<Vacina>>
}