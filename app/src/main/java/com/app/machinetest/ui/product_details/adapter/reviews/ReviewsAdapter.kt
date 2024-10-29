package com.app.machinetest.ui.product_details.adapter.reviews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.machinetest.models.product_details.Review
import com.xpayback.machinetest.databinding.CellProductReviewsBinding
import java.text.SimpleDateFormat
import java.util.Date

class ReviewsAdapter(
   private var list: MutableList<Review?>
): Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    inner class ReviewsViewHolder(private val binding: CellProductReviewsBinding):ViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun onBind(item:Review?) = binding.apply{
            textName.text = item?.reviewerName?:""
            textEmail.text = item?.reviewerEmail?:""
            textComment.text = item?.comment?:""
            productRatingBar.rating = item?.rating?.toFloat()?:0f
            textRating.text = item?.rating?.toFloat()?.toString()
            try {
                val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val format2 = SimpleDateFormat("dd MMM yyyy")
                val date: Date? = item?.date?.let { format1.parse(it) }
                val dateInString  = date?.let { format2.format(it) } ?:""
                textReviewDate.text = dateInString
            }catch (e:Exception){
               textReviewDate.text = "Review date not available"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
       return ReviewsViewHolder(
           CellProductReviewsBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: MutableList<Review?>){
        this.list = list
        notifyDataSetChanged()
    }
}