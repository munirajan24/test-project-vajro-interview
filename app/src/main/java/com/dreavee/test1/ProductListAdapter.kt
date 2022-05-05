package com.dreavee.test1

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dreavee.test1.model.ProductList
import com.dreavee.test1.model.Products
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.OnItemClickListener
import kotlinx.android.synthetic.main.product_item_layout.view.btnAddition
import kotlinx.android.synthetic.main.product_item_layout.view.btnAdd
import kotlinx.android.synthetic.main.product_item_layout.view.btnMinus
import kotlinx.android.synthetic.main.product_item_layout.view.txtQty
import kotlinx.android.synthetic.main.product_item_layout.view.imgProduct
import kotlinx.android.synthetic.main.product_item_layout.view.txtPrice
import kotlinx.android.synthetic.main.product_item_layout.view.txtProductName

class ProductListAdapter(
    private val products: ArrayList<Products>
) : RecyclerView.Adapter<ProductListAdapter.DataViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            product: Products
        ) {
            itemView.txtProductName.text = product.name
            itemView.txtPrice.text = product.price
            Glide.with(itemView.imgProduct.context)
                .load(product.image)
                .into(itemView.imgProduct)

            itemView.btnAdd.setOnClickListener {
                product.productQuantity = 1
                onItemClickListener.onAddClick(product)
            }
            itemView.btnAddition.setOnClickListener {
//                product.productQuantity = product.productQuantity?.plus(1)
                onItemClickListener.onUpdate(product)
            }
            itemView.btnMinus.setOnClickListener {
//                product.productQuantity = product.productQuantity?.minus(1)
                onItemClickListener.onUpdate(product)
            }

            when (product.productQuantity) {
                0, null -> {
                    product.productQuantity = 0
                    itemView.btnAdd.visibility = VISIBLE
                    itemView.btnAddition.visibility = GONE
                    itemView.btnMinus.visibility = GONE
                    itemView.txtQty.visibility = GONE
                }
                else -> {
                    itemView.btnAdd.visibility = GONE
                    itemView.btnAddition.visibility = VISIBLE
                    itemView.btnMinus.visibility = VISIBLE
                    itemView.txtQty.visibility = VISIBLE
                    itemView.txtQty.text = (product.productQuantity ?: 0).toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.product_item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(
            products[position]
        )

    fun addData(list: ProductList) {
        products.clear()
        products.addAll(list.products)
    }

    fun removeProduct(product: Product) {
        try {
            for (product1 in products){
                if (product1.product_id?.equals(product.product_id) == true){
                    products.remove(product1)
                }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

}