package com.dreavee.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dreavee.test1.databinding.ActivityProductDetailsBinding
import com.dreavee.test1.model.Products
import com.dreavee.test1.mvvm.MainRepository
import com.dreavee.test1.mvvm.MyViewModelFactory
import com.dreavee.test1.mvvm.ProductViewModel
import com.dreavee.test1.mvvm.RetrofitService
import com.dreavee.test1.room.CartDatabase
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.Utils
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class STATE_OF_PRODUCT {
    ADDED,
    SUBTRACTED,
    NO_CHANGE
}

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var products: Products

    var stateOfProduct = STATE_OF_PRODUCT.NO_CHANGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService, CartDatabase.getDatabase(this))

        val productJson = getIntent().getStringExtra("product")
        if (productJson != null) {
            val gson = Gson()
            products = gson.fromJson(productJson, Products::class.java)
        }
        productViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        )[ProductViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        binding.txtProductName.text = products.name
        binding.txtPrice.text = products.price
        binding.txtQty.text = (products.productQuantity ?: 0).toString()
        refreshCart()

        Glide.with(binding.imgProduct.context)
            .load(products.image)
            .into(binding.imgProduct)

        binding.btnAddition.setOnClickListener {
            stateOfProduct = STATE_OF_PRODUCT.ADDED
            products.productQuantity = products.productQuantity?.plus(1)
            binding.txtQty.text = (products.productQuantity ?: 0).toString()

            if (products.productQuantity!! > 0) {
                binding.txtCartCount.visibility = VISIBLE
                binding.txtCartCount.text =
                    binding.txtCartCount.text.toString().toInt().plus(1).toString()
            }
        }
        binding.btnMinus.setOnClickListener {
            stateOfProduct = STATE_OF_PRODUCT.SUBTRACTED
            if (products.productQuantity != 0) {
                products.productQuantity = products.productQuantity?.minus(1)
                binding.txtQty.text = (products.productQuantity ?: 0).toString()
            }
            binding.btnAdd.text = if (products.productQuantity != 0) "add" else "update"
            if (products.productQuantity!! == 0) {
                binding.txtCartCount.text =
                    binding.txtCartCount.text.toString().toInt().minus(1).toString()

                binding.txtCartCount.visibility =
                    if (binding.txtCartCount.text.toString().toInt() == 0) GONE else VISIBLE
            }
        }

        binding.btnAdd.setOnClickListener {
            lifecycleScope.launch {
                updateCartProduct(products)
                onBackPressed()
            }

        }

        binding.txtCartCount.setOnClickListener {
            var intent = Intent(this, CartActivity::class.java)
            getResult.launch(intent)
        }
        binding.imgCart.setOnClickListener {
            var intent = Intent(this, CartActivity::class.java)
            getResult.launch(intent)
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            refreshCart()
        }


    private fun refreshCart() {
        lifecycleScope.launch {
            products.productQuantity = getCartProductQuantityById(products.product_id) ?: 0
            val qty = products.productQuantity
            Log.d("productQuantity", "refreshCart: ${products.name} : ${qty}")
            updateCartIconCountFromDB()
        }
    }


    private suspend fun getCartProductQuantityById(product_id: Int?): Int? {
        return productViewModel.getCartProductQuantityById(product_id)
    }

    private suspend fun getCartProductById(product_id: Int?): Product? {
        return productViewModel.getCartProductById(product_id)
    }


    private suspend fun addToCart(product: Products) {

    }

    private suspend fun updateCartProduct(product: Products) {

        when (stateOfProduct) {
            STATE_OF_PRODUCT.NO_CHANGE -> {
                if (product.productQuantity == 0 || product.productQuantity == 1) {
                    productViewModel.addProductsToDB(Utils.apiToDbProduct(product))
                }
            }

            STATE_OF_PRODUCT.ADDED -> {
                if (product.productQuantity == 1) {
                    productViewModel.addProductsToDB(Utils.apiToDbProduct(product))
                } else {
                    productViewModel.updateProductsToDB(Utils.apiToDbProduct(product))
                }
            }

            STATE_OF_PRODUCT.SUBTRACTED -> {
                if (product.productQuantity!! == 0) {
                    productViewModel.removeProductsFromDB(Utils.apiToDbProduct(product))
                } else {
                    productViewModel.updateProductsToDB(Utils.apiToDbProduct(product))
                }
            }
        }

    }


    private suspend fun updateCartIconCountFromDB() {
        productViewModel.getProductsFromCart().collect() {
            if (it.size > 0) {
                binding.txtCartCount.visibility = VISIBLE
                binding.txtCartCount.text = it.size.toString()
            }
        }
    }

}