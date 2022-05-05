package com.dreavee.test1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dreavee.test1.room.entity.Product
import com.dreavee.test1.utils.OnCartItemClickListener
import kotlinx.android.synthetic.main.cart_product_item_layout.view.*

class ProductCartListAdapter(
    private val products: ArrayList<Product>
) : RecyclerView.Adapter<ProductCartListAdapter.DataViewHolder>() {

    lateinit var onItemClickListener: OnCartItemClickListener

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            product: Product
        ) {
            itemView.checkout_item_name.text = product.name
            itemView.checkout_item_price.text = product.price
            Glide.with(itemView.img_child.context)
                .load(product.image)
                .into(itemView.img_child)


            itemView.checkout_item_action_add.setOnClickListener {
                product.productQuantity = product.productQuantity?.plus(1)
                onItemClickListener.onUpdate(product)
            }
            itemView.checkout_item_action_minus.setOnClickListener {
                product.productQuantity = product.productQuantity?.minus(1)
                onItemClickListener.onUpdate(product)
            }

            when (product.productQuantity) {
                0, null -> {
                    product.productQuantity = 0
                    itemView.checkout_item_action_add.visibility = View.GONE
                    itemView.checkout_item_action_minus.visibility = View.GONE
                    itemView.checkout_item_action_qty.visibility = View.GONE
                }
                else -> {
                    itemView.checkout_item_action_add.visibility = View.VISIBLE
                    itemView.checkout_item_action_minus.visibility = View.VISIBLE
                    itemView.checkout_item_action_qty.visibility = View.VISIBLE
                    itemView.checkout_item_action_qty.text = (product.productQuantity ?: 0).toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cart_product_item_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(
            products[position]
        )


    fun addCartData(productList: List<Product>) {
        products.clear()
        products.addAll(productList)
    }

    fun removeProduct(product: Product) {
        try {
            for (product1 in products) {
                if (product1.product_id?.equals(product.product_id) == true) {
                    products.remove(product1)
                }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

}