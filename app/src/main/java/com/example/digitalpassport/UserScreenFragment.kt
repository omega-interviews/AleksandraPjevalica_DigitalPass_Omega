package com.example.digitalpassport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.digitalpassport.databinding.FragmentUserScreenBinding
import com.google.gson.GsonBuilder
import kotlinx.serialization.decodeFromString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.util.Base64
import androidx.fragment.app.viewModels
import kotlinx.serialization.json.Json
import org.json.JSONObject


lateinit var binding: FragmentUserScreenBinding
private lateinit var navController: NavController
private lateinit var passes: Map<String, Pass>
lateinit var listOfPasses: List<Pair<String, Pass>>
lateinit var user: User


/**
 * A simple [Fragment] subclass.
 * Use the [UserScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserScreenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var key1: String? = null
    val viewModel: PassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserScreenBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        val gson = GsonBuilder()
            .registerTypeAdapter(UserResponse::class.java, UserResponseDeserializer())
            .create()

        binding.createAccountButton.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http:192.168.0.17:5005") // replace with your actual base URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val apiService = retrofit.create(ApiService::class.java)



            apiService.createUserAccount().enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val userResponse: UserResponse? = response.body()
                         passes = userResponse?.passes!!
                        listOfPasses = passes.toList()


                        passes?.let { passMap ->


                            for ((key, pass) in passMap) {

                                val name = pass.name
                                val description = pass.description
                                val icon = pass.icon
                                key1 = key
                                // Do something with name, description, and icon

                            }
                        }


                         user = userResponse?.user!!

                        user.let { userData ->
                            val firstName = userData.firstName
                            val lastName = userData.lastName
                            val email = userData.email
                            val image = userData.image

                            // Do something with user data
                        }
                        viewModel.setPassesList(listOfPasses)
                        viewModel.setUser(user)

                        val bundle = Bundle().apply {
                            putSerializable("passList", listOfPasses as Serializable)
                            putParcelable("user", user)

                        }
                        listOfPasses.forEach {

                        }

                        navController.navigate(R.id.action_userScreenFragment_to_passesListScreenFragment, bundle)

                    } else {
                        // Handle error based on response.errorBody()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    // Handle network or other exceptions
                }
            })

            apiService.getCredential(CredentialRequest("TIME")).enqueue(object : Callback<YourResponseType>{
                override fun onResponse(
                    call: Call<YourResponseType>,
                    response: Response<YourResponseType>
                ) {
                    if (response.isSuccessful) {

                        // Handle the response on the main thread, update UI, etc.


//                        val decodedClaims = response.body()?.jwt?.let { it1 -> decodeJwt(it1) }
                        val decodedJwt = response.body()?.jwt?.let { it1 -> decodeJwt(it1) }

                        if (decodedJwt != null) {
                            val (header, payload) = decodedJwt
                            println("Decoded Header: $header")
                            println("Decoded Payload: $payload")
                            val expValue = extractExpFromJwtPayload(payload)
                            println("@@@@@@@@@!!!!!!!!!!!!" + expValue)

                            viewModel.setTime(expValue)
                        } else {
                            println("Invalid JWT format")
                        }

                    }
                    else {

                    }
                }
                override fun onFailure(call: Call<YourResponseType>, t: Throwable) {
                }

            })

            apiService.getCredential(CredentialRequest("READY")).enqueue(object : Callback<YourResponseType>{
                override fun onResponse(
                    call: Call<YourResponseType>,
                    response: Response<YourResponseType>
                ) {
                    if (response.isSuccessful) {

                        // Handle the response on the main thread, update UI, etc.


//                        val decodedClaims = response.body()?.jwt?.let { it1 -> decodeJwt(it1) }
                        val decodedJwt = response.body()?.jwt?.let { it1 -> decodeJwt(it1) }

                        if (decodedJwt != null) {
                            val (header, payload) = decodedJwt
                            println("Decoded Header: $header")
                            println("Decoded Payload: $payload")
                            val expValue = extractExpFromJwtPayload(payload)
                            println("@@@@@@@@@!!!!!!!!!!!!" + expValue)

                            viewModel.setReady(expValue)
                        } else {
                            println("Invalid JWT format")
                        }

                    }
                    else {

                    }
                }
                override fun onFailure(call: Call<YourResponseType>, t: Throwable) {
                }

            })


    }



    }


    fun extractExpFromJwtPayload(jwtPayload: String): Long {
        val payloadJson = JSONObject(jwtPayload)
        return payloadJson.optLong("exp", -1)
    }

    fun decodeJwt(token: String): Pair<String, String>? {
        val parts = token.split(".")

        if (parts.size != 3) {
            // Invalid JWT format
            return null
        }

        val header = decodeBase64Url(parts[0])
        val payload = decodeBase64Url(parts[1])

        return Pair(header, payload)
    }

    fun decodeBase64Url(encoded: String): String {
        val base64 = encoded.replace("-", "+").replace("_", "/") + when (encoded.length % 4) {
            2 -> "=="
            3 -> "="
            else -> ""
        }

        val decodedBytes = Base64.getDecoder().decode(base64)

        return decodedBytes.toString(Charsets.UTF_8)

    }


}

