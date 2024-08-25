package com.io1.e_commerce

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
import com.io1.e_commerce.databinding.FragmentProfileBinding
import com.io1.e_commerce.model.Profile
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
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

            binding.textView10.text = document.getString("email")
            //binding.profilAddress.text = document.getString("address")


            val width = 300
            val height = 300

            if(document.getString("profilImage") == null){
                val profilImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
                Picasso.with(requireContext())
                    .load(profilImage)
                    .resize(width, height)
                    .centerCrop()
                    .into(binding.userProfileImage)
            }else{
                Picasso.with(requireContext())
                    .load(document.getString("profilImage"))
                    .resize(width, height)
                    .centerCrop()
                    .into(binding.userProfileImage)
            }
      }


        binding.settingsContainer.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUpdateFragment()
            view.findNavController().navigate(action)
        }

        binding.orderContainer.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToOrderFragment()
            view.findNavController().navigate(action)
        }



        return view
        }

}

