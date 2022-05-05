package com.dreavee.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var productViewModel: ProductViewModel

    private lateinit var products: Products

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
        ).get(ProductViewModel::class.java)

        setupUI()
    }

    private fun setupUI() {
        binding.txtProductName.text = products.name
        binding.txtPrice.text = products.price
        binding.txtQty.text = (products.productQuantity ?: 0).toString()
        Glide.with(binding.imgProduct.context)
            .load(products.image)
            .into(binding.imgProduct)

        binding.btnAddition.setOnClickListener {
            products.productQuantity = products.productQuantity?.plus(1)
            binding.txtQty.text = (products.productQuantity ?: 0).toString()

        }
        binding.btnMinus.setOnClickListener {
            if (products.productQuantity != 0) {
                binding.btnAdd.text = "add"
                products.productQuantity = products.productQuantity?.minus(1)
                binding.txtQty.text = (products.productQuantity ?: 0).toString()
            } else {
                binding.btnAdd.text = "update"
            }

        }

        binding.btnAdd.setOnClickListener {

            lifecycleScope.launch {
                addToCart(products)
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
            Log.d("productQuantity", "setupObserver: ${products.name} : ${qty}")
            getFromCart()
        }
    }


    private suspend fun getCartProductQuantityById(product_id: Int?): Int? {
        return productViewModel.getCartProductQuantityById(product_id)
    }

    private suspend fun getCartProductById(product_id: Int?): Product? {
        return productViewModel.getCartProductById(product_id)
    }


    private suspend fun addToCart(product: Products) {
        productViewModel.addProductsToDB(Utils.apiToDbProduct(product))
    }

    private suspend fun updateCartProduct(product: Products) {
        if (product.productQuantity == 0) {
            productViewModel.removeProductsFromDB(Utils.apiToDbProduct(product))
        } else {
            productViewModel.updateProductsToDB(Utils.apiToDbProduct(product))
        }
    }


    private suspend fun getFromCart() {
        productViewModel.getProductsFromCart().collect() {
            if (it.size > 0) {
                binding.txtCartCount.visibility = VISIBLE
                binding.txtCartCount.text = it.size.toString()
            }
        }
    }

}