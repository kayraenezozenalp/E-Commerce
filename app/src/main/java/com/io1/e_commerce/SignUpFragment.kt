package com.io1.e_commerce

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        binding.textView.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            view.findNavController().navigate(action)
        }
        binding.button.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()

            val profilImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = hashMapOf(
                                "profilImage" to profilImage,
                                "email" to email,
                                "password"  to pass
                            )
                            val uid = firebaseAuth.currentUser?.uid

                            db.collection("users")
                                .document(uid!!)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(),"User succesfully created",Toast.LENGTH_LONG).show()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                                }
                            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                            view.findNavController().navigate(action)
                        } else {
                            Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
        return view
    }


}