package com.example.digitalpassport

import android.os.Parcelable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.lang.reflect.Type
import kotlinx.serialization.Serializable


interface ApiService {
    @POST("/account")
    fun createUserAccount(): Call<UserResponse>

    // New GET request for credentials
    @POST("/credential")
    fun getCredential(@Body request: CredentialRequest): Call<YourResponseType>


}


data class CredentialRequest(
    val type: String
)

data class YourResponseType(val type: String, val jwt: String)

// Define Parcelable data class
@Parcelize
data class PassList(val passes: List<Map<String,Pass>>) : Parcelable

data class UserResponse(
    val passes: Map<String, Pass>?,
    val user: User?
)
@Parcelize
data class Pass(
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("icon")
    val icon: String?
):Parcelable

@Parcelize
data class User(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("image")
    val image: String?
):Parcelable

operator fun Map<String, Pass>.component1(): List<String> {
    return this.keys.toList()
}

operator fun Map<String, Pass>.component2(): List<Pass> {
    return this.values.toList()
}

@Serializable
data class JwtPayload(
    val sub: String,
    val jti: String,
    val iat: Long,
    val exp: Long,
    val type: String
)

class UserResponseDeserializer : JsonDeserializer<UserResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserResponse {
        val jsonObject = json?.asJsonObject
        val passesMap = mutableMapOf<String, Pass>()

        jsonObject?.entrySet()?.forEach { entry ->
            if (entry.key.startsWith("pass")) {
                val pass = context?.deserialize<Pass>(entry.value, Pass::class.java)
                passesMap[entry.key] = pass ?: Pass(null, null, null)
            }
        }

        val user = context?.deserialize<User>(jsonObject?.get("user"), User::class.java)

        return UserResponse(passesMap, user)
    }
}

