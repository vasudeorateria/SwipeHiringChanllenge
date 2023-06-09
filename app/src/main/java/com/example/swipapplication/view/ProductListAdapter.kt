package com.example.swipapplication.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swipapplication.R
import com.example.swipapplication.databinding.ItemProductLayoutBinding
import com.example.swipapplication.model.responses.ProductItem
import com.squareup.picasso.Picasso

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {

    private val productList: MutableList<ProductItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProductList(productList: List<ProductItem>) {
        this.productList.clear()
        this.productList.addAll(productList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    inner class ProductListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding: ItemProductLayoutBinding = ItemProductLayoutBinding.bind(itemView)

        fun bind(product: ProductItem) {
            binding.apply {
                if (!product.image.isNullOrEmpty()) {
                    Picasso.get().load(product.image)
                        .placeholder(R.drawable.product_iomage_placeholder)
                        .error(R.drawable.product_iomage_placeholder)
                        .into(productImage)
                }
                productName.text = product.product_name
                productPrice.text = "Price: ${product.price.toString()}"
                productType.text = "Type: ${product.product_type}"
                productTax.text = "Tax: ${product.tax.toString()}"
            }
        }
    }
}