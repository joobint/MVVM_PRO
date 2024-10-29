package com.app.machinetest.ui.product_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.machinetest.models.products.Product
import com.app.machinetest.ui.product_list.adapter.ProductAdapter.ProductViewHolder
import com.xpayback.machinetest.R
import com.xpayback.machinetest.databinding.CellProductBinding

class ProductAdapter(
   private var list: List<Product?>
): RecyclerView.Adapter<ProductViewHolder>() {

    var onClickOnItem:((Product)->Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
       return ProductViewHolder(
           CellProductBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
       holder.bind(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    inner class ProductViewHolder(val binding: CellProductBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(product: Product?) = binding.apply{
            textProductName.text = product?.title
            textPrice.text = "$${product?.price}"
            productRatingBar.rating = product?.rating?.toFloat() ?: 0f

            Glide.with(root.context)
                .load(product?.images?.get(0))
                .placeholder(R.drawable.place_holder)
                .into(imgProduct)

            root.setOnClickListener {
                product?.let { it1 -> onClickOnItem?.invoke(it1) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Product?>){
        this.list = list
        notifyDataSetChanged()
    }
}