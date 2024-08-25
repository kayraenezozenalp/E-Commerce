package com.io1.e_commerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.io1.e_commerce.databinding.ActivitySignInBinding

class SignINActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.passET.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val user = firebaseAuth.currentUser
                            user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                                if (tokenTask.isSuccessful) {
                                    // JWT tokenını alın
                                    val token = tokenTask.result?.token
                                    // Token'ı kullanabilirsiniz
                                    println("JWT Token: $token")
                                } else {
                                    // Token alınamazsa hata durumunu işleyin
                                    println("Token alınamadı: ${tokenTask.exception}")
                                }
                            }
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
            }else{
                Toast.makeText(this,"Please fill in the blank spaces", Toast.LENGTH_LONG).show()
            }
        }

    }
}