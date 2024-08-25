package com.io1.e_commerce

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore



        binding.textView.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            view.findNavController().navigate(action)
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
                                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                view.findNavController().navigate(action)
                                println("JWT Token: $token")
                            } else {
                                // Token alınamazsa hata durumunu işleyin
                                println("Token alınamadı: ${tokenTask.exception}")
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(),it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Please fill in the blank spaces", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }


}