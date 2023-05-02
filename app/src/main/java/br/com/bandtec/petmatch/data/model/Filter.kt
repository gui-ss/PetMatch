package br.com.bandtec.petmatch.data.model

import com.google.gson.annotations.SerializedName

data class Filter(

    @SerializedName("idade") var petIdade: String?,

    @SerializedName("nome") var petNome: String?,

    @SerializedName("especie") var petEspecie: String?,

    @SerializedName("raca") var petRaca: String?,

    @SerializedName("porte") var petPorte: String?,

    @SerializedName("sexo") var petSexo: String?,

    @SerializedName("castrado") var petCastrado: String?,

    @SerializedName("sociavel") var petSociavel: String?,

    @SerializedName("comportamento") var petComportamento: String?,

    @SerializedName("parametros") var parametros: String?

)
