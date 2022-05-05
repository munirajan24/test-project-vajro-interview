package com.dreavee.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dreavee.test1.databinding.ActivityProductListBinding
import com.dreavee.test1.model.ProductList
import com.dreavee.test1.model.Products
import com.dreavee.test1.mvvm.MainRepository
import com.dreavee.test1.mvvm.MyViewModelFactory
import com.dreavee.test1.mvvm.ProductViewModel
import com.dreavee.test1.mvvm.RetrofitService
import com.dreavee.test1.room.CartDatabase
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.OnItemClickListener
import com.dreavee.test1.utils.Status
import com.dreavee.test1.utils.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapter: ProductListAdapter

    private lateinit var products: ProductList
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService, CartDatabase.getDatabase(this))
        products = ProductList(arrayListOf())

        productViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(ProductViewModel::class.java)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductListAdapter(arrayListOf())

        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onAddClick(products: Products) {
                lifecycleScope.launch {
//                    addToCart(products)
                    val gson = Gson()
                    val passableObject = gson.toJson(products)
                    val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                    intent.putExtra("product", passableObject)
                    getResult.launch(intent)
                }
            }

            override fun onUpdate(products: Products) {
                lifecycleScope.launch {
//                    updateCartProduct(products)
                    val gson = Gson()
                    val passableObject = gson.toJson(products)
                    val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                    intent.putExtra("product", passableObject)
                    getResult.launch(intent)
                }
            }

        }

        binding.recyclerView.adapter = adapter


        binding.txtCartCount.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            getResult.launch(intent)
        }
        binding.imgCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
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

    private fun setupObserver() {
        productViewModel.getProducts().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { products ->
                        this.products = products
                        refreshCart()
                    }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun refreshCart() {
        lifecycleScope.launch {
            for (product in products.products) {
                product.productQuantity = getCartProductQuantityById(product.product_id) ?: 0
                val qty = product.productQuantity
                Log.d("productQuantity", "setupObserver: ${product.name} : ${qty}")
            }
            getFromCart()
            adapter.notifyDataSetChanged()
        }
        renderProductList(products)
    }

    private fun renderProductList(productList: ProductList) {
        adapter.addData(productList)
        adapter.notifyDataSetChanged()
    }


    private suspend fun addToCart(product: Products) {
        productViewModel.addProductsToDB(Utils.apiToDbProduct(product))
        adapter.notifyDataSetChanged()
    }

    private suspend fun getCartProductQuantityById(product_id: Int?): Int? {
        return productViewModel.getCartProductQuantityById(product_id)
    }

    private suspend fun getCartProductById(product_id: Int?): Product? {
        return productViewModel.getCartProductById(product_id)
    }

    private suspend fun removeFromCart(product: Products) {
        productViewModel.removeProductsFromDB(Utils.apiToDbProduct(product))
        adapter.notifyDataSetChanged()
    }


    private suspend fun updateCartProduct(product: Products) {
        if (product.productQuantity == 0) {
            productViewModel.removeProductsFromDB(Utils.apiToDbProduct(product))
//            adapter.removeProduct(Utils.apiToDbProduct(product))
            adapter.notifyDataSetChanged()
        } else {
            productViewModel.updateProductsToDB(Utils.apiToDbProduct(product))
            adapter.notifyDataSetChanged()
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