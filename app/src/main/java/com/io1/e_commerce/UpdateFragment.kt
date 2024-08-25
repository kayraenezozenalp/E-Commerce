package com.io1.e_commerce

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.databinding.FragmentUpdateBinding
import com.squareup.picasso.Picasso
import java.net.URI


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var firebaseAuth : FirebaseAuth
    val db = Firebase.firestore
    var imageUri : Uri? = null
    var email : String = ""
    var password : String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")

            val width = 130
            val height = 130
            imageUri = uri

            Picasso.with(requireContext())
                .load(uri)
                .resize(width, height)
                .centerCrop()
                .into(binding.imageView)

        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater,container,false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser?.uid

        val docRef = db.collection("users").document(currentUser!!)

        docRef.get().addOnSuccessListener { document ->

            db.collection("users")
                .document(currentUser)
                .get()
                .addOnSuccessListener { result ->
                    System.out.println(result)
                }
                .addOnFailureListener { exception ->
                }

            email = document.getString("email").toString()
            password = document.getString("password").toString()

            val width = 130
            val height = 130

            if(document.getString("profilImage") == null){
                val profilImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
                Picasso.with(requireContext())
                    .load(profilImage)
                    .resize(width, height)
                    .centerCrop()
                    .into(binding.imageView)
            }else{
                Picasso.with(requireContext())
                    .load(document.getString("profilImage"))
                    .resize(width, height)
                    .centerCrop()
                    .into(binding.imageView)
            }

        }

            binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.button4.setOnClickListener {
                val user = hashMapOf(
                    "fullName" to binding.fullName.text.toString(),
                    "profilImage" to imageUri,
                    "email" to email,
                    "password" to password,
                    "phone" to binding.phone.text.toString(),
                    "address" to binding.address.text.toString(),
                )

                db.collection("users")
                    .document(currentUser)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(),"User succesfully created",Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                    }


            }

        /*    currentUser.updateProfile(profilUpdate)?.addOnSuccessListener {
                Toast.makeText(requireContext(),"Updated",Toast.LENGTH_LONG).show()
            }?.addOnFailureListener {
                Toast.makeText(requireContext(),"Update Error!",Toast.LENGTH_LONG).show()
            }

         */


        return view
    }


}