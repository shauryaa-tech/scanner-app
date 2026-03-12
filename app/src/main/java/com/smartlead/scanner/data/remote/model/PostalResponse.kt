package com.smartlead.scanner.data.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostalResponse(
    @SerializedName("Message")
    @SerialName("Message")
    val message: String? = null,
    @SerializedName("Status")
    @SerialName("Status")
    val status: String? = null,
    @SerializedName("PostOffice")
    @SerialName("PostOffice")
    val postOffice: List<PostOffice>? = null
)

@Serializable
data class PostOffice(
    @SerializedName("Name")
    @SerialName("Name")
    val name: String? = null,
    @SerializedName("Description")
    @SerialName("Description")
    val description: String? = null,
    @SerializedName("BranchType")
    @SerialName("BranchType")
    val branchType: String? = null,
    @SerializedName("DeliveryStatus")
    @SerialName("DeliveryStatus")
    val deliveryStatus: String? = null,
    @SerializedName("Circle")
    @SerialName("Circle")
    val circle: String? = null,
    @SerializedName("District")
    @SerialName("District")
    val district: String? = null,
    @SerializedName("Division")
    @SerialName("Division")
    val division: String? = null,
    @SerializedName("Region")
    @SerialName("Region")
    val region: String? = null,
    @SerializedName("Block")
    @SerialName("Block")
    val block: String? = null,
    @SerializedName("State")
    @SerialName("State")
    val state: String? = null,
    @SerializedName("Country")
    @SerialName("Country")
    val country: String? = null,
    @SerializedName("Pincode")
    @SerialName("Pincode")
    val pincode: String? = null
)
