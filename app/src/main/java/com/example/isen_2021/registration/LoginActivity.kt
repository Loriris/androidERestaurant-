package com.example.isen_2021.registration

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.isen_2021.R
import com.example.isen_2021.databinding.ActivityLoginBinding
import com.example.isen_2021.network.NetworkConstant
import com.example.isen_2021.network.RegisterResult
import com.example.isen_2021.network.User
import com.google.gson.GsonBuilder
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logButton.setOnClickListener {
            if(verifyInformations()) {
                launchRequest()
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun launchRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = NetworkConstant.BASE_URL + NetworkConstant.PATH_LOGIN

        val jsonData = JSONObject()
        jsonData.put(NetworkConstant.ID_SHOP, "1")
        jsonData.put(NetworkConstant.EMAIL, binding.email.text)
        jsonData.put(NetworkConstant.PASSWORD, binding.password.text)

        var request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonData,
            { response ->
                val userResult = GsonBuilder().create().fromJson(response.toString(), RegisterResult::class.java)
                saveUser(userResult.data)
            },
            { error ->
                error.message?.let {
                    Log.d("request", it)
                } ?: run {
                    Log.d("request", error.toString())
                    Log.d("request", String(error.networkResponse.data))
                }
            }
        )
        queue.add(request)
    }

    fun verifyInformations(): Boolean {
        return (binding.email.text?.isNotEmpty() == true &&
                binding.password.text?.count() ?: 0 >= 6)
    }

    fun saveUser(user: User) {
        val sharedPreferences = getSharedPreferences(RegisterActivity.USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(RegisterActivity.ID_USER, user.id)
        editor.apply()

        setResult(Activity.RESULT_FIRST_USER)
        finish()
    }
}