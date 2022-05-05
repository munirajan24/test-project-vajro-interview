package com.dreavee.test1

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreavee.test1.databinding.ActivityCartBinding
import com.dreavee.test1.mvvm.MainRepository
import com.dreavee.test1.mvvm.MyViewModelFactory
import com.dreavee.test1.mvvm.ProductViewModel
import com.dreavee.test1.mvvm.RetrofitService
import com.dreavee.test1.room.CartDatabase
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.OnCartItemClickListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productCartListAdapter: ProductCartListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService, CartDatabase.getDatabase(this))
        productViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(ProductViewModel::class.java)


        setupUI()
        setupObserver()

    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        productCartListAdapter = ProductCartListAdapter(arrayListOf())

        productCartListAdapter.onItemClickListener = object : OnCartItemClickListener {
            override fun onAddClick(products: Product) {

                lifecycleScope.launch {
                    addToCart(products)
                }
            }

            override fun onUpdate(products: Product) {
                lifecycleScope.launch {
                    updateCartProduct(products)
                }
            }

        }
        binding.recyclerView.adapter = productCartListAdapter
        binding.recyclerView.visibility = VISIBLE
        binding.progressBar.visibility = GONE


        binding.imgHome.setOnClickListener { view ->
            onBackPressed()
        }
        binding.imgClose.setOnClickListener { view ->
            onBackPressed()
        }
        binding.totalLayout.setOnClickListener { view ->
            if (binding.moreDetail.visibility != GONE) {
                binding.imgToggle.isSelected = false
                binding.btm.setBackgroundResource(R.color.md_grey_50)
                binding.moreDetail.visibility = GONE
            } else {
                binding.btm.setBackgroundResource(R.color.green_2)
                binding.imgToggle.isSelected = true
                binding.moreDetail.visibility = VISIBLE
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            getFromCart()
        }
    }

    private suspend fun getFromCart() {
        productViewModel.getProductsFromCart().collect() {
            calculateTotal(it)
            renderCartProductList(it)
        }
    }

    private fun calculateTotal(it: List<Product>) {
        var subtotal = 0.0
        for (prod in it) {
            val price = prod.price?.filter { it.isDigit() }
            subtotal += (price?.toDouble()!!) * (prod.quantity!!)
        }
        val tax = subtotal * .05
        val service = 42
        val total = subtotal + tax + service

        binding.subTotal.text = "₹ ${String.format("%.2f", subtotal)}"
        binding.taxValue.text = "₹ ${String.format("%.2f", tax)}"
        binding.serviceCharge.text = "₹ $service"
        binding.totalPrice.text = "₹ ${String.format("%.2f", total)}"
    }

    private fun renderCartProductList(productList: List<Product>) {
        productCartListAdapter.addCartData(productList)
        productCartListAdapter.notifyDataSetChanged()
    }


    private suspend fun addToCart(product: Product) {
        productViewModel.addProductsToDB(product)
    }

    private suspend fun updateCartProduct(product: Product) {
        if (product.productQuantity == 0) {
            productViewModel.removeProductsFromDB(product)
            productCartListAdapter.removeProduct(product)
        } else {
            productViewModel.updateProductsToDB(product)
            productCartListAdapter.notifyDataSetChanged()
        }
    }


}

private fun String?.replace(s: String, s1: String, s2: String, s3: String): Any {
    this?.replace(s, s1)
    this?.replace(s2, s3)
    return this!!
}
