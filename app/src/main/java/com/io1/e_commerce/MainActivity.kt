package com.io1.e_commerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.io1.e_commerce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.navViewBottom

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{controller, destination, argument ->
            when(destination.id){
                R.id.homeFragment,R.id.profileFragment,R.id.searchProductFragment,R.id.paymentFragment ->{
                    binding.navViewBottom.visibility = View.VISIBLE
                }
                R.id.detailFragment, R.id.loginFragment,R.id.signUpFragment,R.id.updateFragment->{
                    binding.navViewBottom.visibility = View.GONE
                }

            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
    }
}