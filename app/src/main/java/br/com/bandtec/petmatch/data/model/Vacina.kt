package br.com.bandtec.petmatch.data.model

import com.google.gson.annotations.SerializedName

data class Vacina(
    @SerializedName("id") val id: String?,

    @SerializedName("nome") val vacNome: String?,

    @SerializedName("data") val vacData: String?,

    @SerializedName("dose") val vacDose: Int?,

    @SerializedName("idPet") val idPet: Int?
)
