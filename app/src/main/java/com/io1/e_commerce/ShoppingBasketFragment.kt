package com.io1.e_commerce

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import java.util.UUID
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.io1.e_commerce.databinding.FragmentShoppingBasketBinding
import com.stripe.android.CustomerSession
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentController
import com.stripe.android.PaymentSession
import com.stripe.android.PaymentSessionConfig
import com.stripe.android.PaymentSessionData
import com.stripe.android.Stripe
import com.stripe.android.core.exception.StripeException
import com.stripe.android.model.ShippingInformation
import com.stripe.android.model.ShippingMethod
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.github.kittinunf.result.Result
import com.google.gson.JsonObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.io1.e_commerce.adapter.BasketAdapter
import com.io1.e_commerce.model.FirebaseResponse
import com.stripe.android.view.PaymentMethodsActivityStarter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


interface StripeService {
    @POST("customers")
    suspend fun createCustomer(@Header("Authorization") authorization : String): Response<String>
}
data class ApiResponse(
    val data: JsonObject
)



class ShoppingBasketFragment : Fragment() {

    private lateinit var binding: FragmentShoppingBasketBinding
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String
    lateinit var paymentSheet: PaymentSheet
    lateinit var customerId: String
    lateinit var epherical: String
    lateinit var clientSecret: String
    var isButtonPressed : Boolean = true
    var userAdrress = ""
    val currentTime = Calendar.getInstance().time

    // Tarih formatını belirle
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    // Zamanı belirtilen formata göre biçimlendir ve yazdır
    val formattedTime = dateFormat.format(currentTime)


    val db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth

    var basketPrice : Int = 0
    private lateinit var newRecyclerView: RecyclerView

    lateinit var retrievedValues: List<FirebaseResponse>


    val secretKey = "sk_test_51PDA9jFgEloRBK4NvWoAmzx0WAjiPuSmkMhiNE7wYbg3YhtYl6tGm0Y0QsAg6now1RpT5VtnaXhNg1cys9ghj0wy00uVRbYt9r"
    val publishableKey = "pk_test_51PDA9jFgEloRBK4NrhYQ0sK8aJcVgO3x0Egp7UIeh0mq9NL6KcNdhhOPpGCG1vOADydPh83n7gWNZyNsk8j4ylQc00YTINs9sv"



    private var isClientSecretInitialized = false
    private var isCustomerIdInitialized = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PaymentConfiguration.init(requireContext(), publishableKey)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        val sharedPreferences =
            this.requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)


        newRecyclerView = binding.recyclerView
        newRecyclerView.layoutManager = LinearLayoutManager(this.context)
        newRecyclerView.setHasFixedSize(true)

        if (DataRetriever.getValues().isNotEmpty()) {
            retrievedValues = DataRetriever.getValues()
            retrievedValues = retrievedValues.filter { it.ProductAmount > 0 }
            getUserData()
            retrievedValues.forEach {
                basketPrice += it.ProductAmount * it.ProductPrice.toInt()
            }
            if(retrievedValues.isEmpty()){
                binding.empty.visibility= View.VISIBLE
            }else{
                binding.empty.visibility= View.GONE
            }
            BasketRetriver.values = basketPrice
            System.out.println("amount " + basketPrice)
        }

        val editor = sharedPreferences.edit()
        editor.putString("basket", "1")
        editor.apply()

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser?.uid

        val docRef = db.collection("users").document(currentUser!!)


        docRef.get().addOnSuccessListener { document ->

            db.collection("users")
                .document(currentUser)
                .get()
                .addOnSuccessListener { result ->

                }
                .addOnFailureListener { exception ->
                }
            userAdrress = document.getString("address").toString()
        }



        val value = sharedPreferences.getString("basket", "varsayılan_değer")
        System.out.println(value)

        if (DataRetriever.getValues().isNotEmpty()) {
            System.out.println("amount " + basketPrice)
            binding.empty.visibility = View.GONE
        }else{
            binding.empty.visibility = View.VISIBLE
        }



        binding.button7.setOnClickListener {
            initCustomerConfiguration()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingBasketBinding.inflate(inflater,container,false)
        val view = binding.root


        return view

    }

    private fun paymentFlow() {
        paymentSheet.presentWithPaymentIntent(clientSecret,PaymentSheet.Configuration("E-Commerce",PaymentSheet.CustomerConfiguration(
            customerId,
            epherical
        )))
    }



    private fun initCustomerConfiguration() {
        val url = "https://api.stripe.com/v1/customers"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    customerId = jsonObject.getString("id")
                    getEmphericalKey()
                    // İşlemler buraya eklenebilir
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Hata durumunda işlenecek kodlar buraya yazılabilir
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $secretKey"
                return headers
            }
        }

        val requestQueu = Volley.newRequestQueue(this.activity)

        requestQueu.add(stringRequest)


    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                System.out.println("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                System.out.println("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                System.out.println("Completed")
            }
        }
    }

    private fun getEmphericalKey(){
        val url = "https://api.stripe.com/v1/ephemeral_keys"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    epherical = jsonObject.getString("secret")
                    getClientSecret(customerId,epherical)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Hata durumunda işlenecek kodlar buraya yazılabilir
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $secretKey"
                headers["Stripe-Version"] = "2024-04-10"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("customer",customerId)
                return params
            }
        }

        val requestQueu = Volley.newRequestQueue(this.activity)

        requestQueu.add(stringRequest)

    }

    private fun getClientSecret(customerID : String,emphericalKey:String) {
        val url = "https://api.stripe.com/v1/payment_intents"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    clientSecret = jsonObject.getString("client_secret")
                    paymentFlow()
                    val order = hashMapOf(
                        "OrderNum" to UUID.randomUUID(),
                        "OrderData" to formattedTime,
                        "OrderAddress" to userAdrress,
                        "OrderPrice" to BasketRetriver.values
                    )
                    db.collection("orders")
                        .add(order)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }



                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Hata durumunda işlenecek kodlar buraya yazılabilir
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $secretKey"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                System.out.println(basketPrice)
                params.put("customer",customerId)
                params.put("amount",BasketRetriver.values.toString()+ "00")
                params.put("currency","eur")
                params.put("automatic_payment_methods[enabled]","true")
                return params
            }
        }

        val requestQueu = Volley.newRequestQueue(this.activity)

        requestQueu.add(stringRequest)

    }

    fun getUserData() {
        newRecyclerView.adapter = BasketAdapter(retrievedValues)
        newRecyclerView.adapter?.notifyDataSetChanged()
    }


    private fun presentPaymentSheet(
        paymentSheet: PaymentSheet,
        customerConfig: PaymentSheet.CustomerConfiguration,
        paymentIntentClientSecret: String
    ) {
        System.out.println("denedim")
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "My merchant name",
                customer = customerConfig,
                // Set `allowsDelayedPaymentMethods` to true if your business handles
                // delayed notification payment methods like US bank accounts.
                allowsDelayedPaymentMethods = true
            )
        )
    }

}

object BasketRetriver {
    var values: Int = 0
    var isChange : Boolean = false

    fun addValue(value: Int) {
        values = values + value
    }
}
