package com.io1.e_commerce

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.databinding.FragmentDetailBinding
import com.io1.e_commerce.model.FirebaseResponse
import com.squareup.picasso.Picasso


class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var binding : FragmentDetailBinding
    val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    val basketList = ArrayList<FirebaseResponse>()
    var isFirst = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        val view = binding.root

        val name = args.name
        val price = args.price
        val stock = args.stock
        val image = args.image
        val productID = args.id
        val isHome = args.isHome
        val productAmount = 0

        firebaseAuth = FirebaseAuth.getInstance()
        val userid = firebaseAuth.currentUser?.uid

        val sharedPreferences = this.requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val value = sharedPreferences.getString("basket", "Sepetiniz boş")

        binding.productNameTextView.text = name
        binding.productQuantityTextView.text = productAmount.toString()
        binding.productPriceTextView.text = price
        binding.productQuantityText.text = productAmount.toString()

        val width = 130
        val height = 130

        Picasso.with(requireContext())
            .load(image)
            .resize(width, height)
            .into(binding.specificProductImage)

        var productList = arrayListOf<FirebaseResponse>()
    /*    if(DataRetriever.getValues() != null){
            productList = DataRetriever.getValues() as ArrayList<FirebaseResponse>
        }


     */

        val product = FirebaseResponse(productID,image,price,stock,name,productAmount)
        val productt = FirebaseResponse("2",image,price,stock,"sdad",productAmount)




        binding.productQuantityPlus.setOnClickListener {
            val retrievedValues: List<FirebaseResponse> = DataRetriever.getValues()
            System.out.println(productList)
            System.out.println("elimdeki id" +productID)
            if(retrievedValues.isNotEmpty()){
                retrievedValues.forEach {
                    System.out.println("gelen id" +it.ProductID)
                    if(it.ProductID == productID){
                        System.out.println("forEach içinde if içinde")
                        val newAmount = it.ProductAmount + 1
                        binding.productQuantityText.text = newAmount.toString()
                        val produc = FirebaseResponse(productID,image,price,stock,name,newAmount)
                        DataHolder.addOrUpdateValue(produc)
                        isFirst = false
                        System.out.println(DataRetriever.getValues())
                    }
                }
                if(isFirst == true){
                    System.out.println("true")
                    binding.productQuantityText.text = (productAmount+1).toString()
                    val produc = FirebaseResponse(productID,image,price,stock,name,productAmount+1)
                    DataHolder.addOrUpdateValue(produc)
                }

            }else{
                System.out.println("empty")
                binding.productQuantityText.text = (productAmount+1).toString()
                val produc = FirebaseResponse(productID,image,price,stock,name,productAmount+1)
                DataHolder.addOrUpdateValue(produc)
            }
        }



        binding.backButton.setOnClickListener {
            if (isHome){
                val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
                view.findNavController().navigate(action)
            }else{
                val action = DetailFragmentDirections.actionDetailFragmentToSearchProductFragment()
                view.findNavController().navigate(action)
            }
        }

        binding.addToBasketButton.setOnClickListener{
            if (isHome){
                val action = DetailFragmentDirections.actionDetailFragmentToHomeFragment()
                view.findNavController().navigate(action)
            }else{
                val action = DetailFragmentDirections.actionDetailFragmentToSearchProductFragment()
                view.findNavController().navigate(action)
            }
        }

    /*    binding.productQuantityMinus.setOnClickListener {
            val retrievedValues: List<FirebaseResponse> = DataRetriever.getValues()
            if(retrievedValues.isNotEmpty()){
                retrievedValues.forEach {
                    if(it.ProductID == productID){
                        val produc = FirebaseResponse(productID,image,price,stock,name,it.ProductAmount)
                        DataHolder.removeValue(produc)
                    }
                }
            }

        }


     */
        binding.productQuantityMinus.setOnClickListener {
            System.out.println("buraya girdim")
            val retrievedValues: List<FirebaseResponse> = DataRetriever.getValues()
            if(retrievedValues.isNotEmpty()){
                System.out.println("buraya girdim")
                retrievedValues.forEach {
                    System.out.println("buraya giriyom")
                    if(it.ProductID == productID && it.ProductAmount != 0){
                        System.out.println("burasdsjgldksögjçds giriyom")
                        val newAmount = it.ProductAmount - 1
                        binding.productQuantityText.text = newAmount.toString()
                        System.out.println("eksi amout " + newAmount.toString())
                        val produc = FirebaseResponse(productID,image,price,stock,name,newAmount)
                        if(newAmount == 0){
                            DataHolder.removeValue(produc)
                        }else {
                            DataHolder.addOrUpdateValue(produc)
                        }
                        isFirst = false
                        System.out.println(DataRetriever.getValues())
                    }
                }
            }
        }


        /*  System.out.println("backet")
         System.out.println(basketList)
         System.out.println(basketList[0].get(0))
        for(i in basketList ){
             System.out.println(amount)
             System.out.println(name)
         }



        basketList.forEach {
            for(i in it){
                System.out.println(name)
            }
        }
*/



        return view
    }


}

object DataHolder {
    var values: MutableList<FirebaseResponse> = mutableListOf()

    fun addValue(value: FirebaseResponse) {
        values.add(value)
    }

    fun addOrUpdateValue(value: FirebaseResponse) {
        val existingIndex =
            values.indexOfFirst { it.ProductID == value.ProductID } // Örneğin, FirebaseResponse içinde bir id değeri varsayalım
        if (existingIndex != -1) {
            // Değer zaten var, güncelle
            values[existingIndex].ProductAmount = value.ProductAmount
        } else {
            // Değer yok, ekle
            values.add(value)
        }
    }

    fun removeValue(value: FirebaseResponse) {
        val existingIndex =
            values.indexOfFirst { it.ProductID == value.ProductID } // Örneğin, FirebaseResponse içinde bir id değeri varsayalım
        if (value.ProductAmount - 1 == 0) {
            values.remove(value)
            System.out.println(DataRetriever.getValues())
        } else {
            if(existingIndex != -1){
            values[existingIndex].ProductAmount = value.ProductAmount-1
            System.out.println(DataRetriever.getValues())
            }
        }

    }


}
object DataRetriever {
    fun getValues(): List<FirebaseResponse> {
        return DataHolder.values.toList()
    }
}