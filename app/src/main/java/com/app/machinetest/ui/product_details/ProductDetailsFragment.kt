package com.app.machinetest.ui.product_details

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.app.machinetest.ui.product_details.adapter.reviews.ReviewsAdapter
import com.app.machinetest.utils.NetworkResult
import com.app.machinetest.utils.hide
import com.app.machinetest.utils.show
import com.app.machinetest.utils.showToast
import com.xpayback.machinetest.R
import com.xpayback.machinetest.databinding.FragmentProductDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round


class ProductDetailsFragment :Fragment(){

    private val args: ProductDetailsFragmentArgs by navArgs()
    private lateinit var binding: FragmentProductDetailsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val viewModel by viewModels<ProductDetailsViewModel>(
        factoryProducer = {
            ProductDetailsViewModelFactory()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.productId = args.productId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = binding.apply {
        observeProductDetails()
        viewModel.productId?.let {
            viewModel.fetchProductDetails()
        }
        reviewsAdapter = ReviewsAdapter(mutableListOf())
        recyclerviewReviews.adapter = reviewsAdapter
        imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showProgressBar() = binding.apply{
          progressBar.show()
    }

    private fun hideProgressBar() = binding.apply {
        progressBar.hide()
    }

    @SuppressLint("SetTextI18n")
    private fun observeProductDetails() {

        lifecycleScope.launch(Dispatchers.Main){

            viewModel.productDetailsResponseLiveData.observe(viewLifecycleOwner){

                when(it){

                    is NetworkResult.Loading->{
                        showProgressBar()
                    }

                    is NetworkResult.Success->{
                        binding.apply {
                            Glide.with(root.context)
                                .load(it.data?.images?.get(0))
                                .placeholder(R.drawable.place_holder)
                                .into(productImage)

                            textProductName.text = it.data?.title
                            textProductDescription.text = it.data?.description
                            productRatingBar.rating = it.data?.rating?.toFloat() ?: 0f
                            textRatingText.text = it.data?.rating.toString()
                            textOriginalPrice.text = "$${it.data?.price}"
                            textOriginalPrice.paintFlags = textOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            val discountPrice = (it.data?.price?:0.0) * ((it.data?.discountPercentage?:0.0)/100)
                            val roundedDiscountPrice = round(discountPrice)
                            textDiscountPrice.text = "$$roundedDiscountPrice"
                            textDiscountPercentage.text = "${it.data?.discountPercentage}% off"

                            val stock = it.data?.stock?:0
                            if (stock<=10){
                                textAvailableStock.text = "Only $stock items left"
                            }else{
                                textAvailableStock.text = "$stock items in stock"
                            }

                            textReturnPolicy.text = it.data?.returnPolicy ?: ""
                            textWarrantyInfo.text = it.data?.warrantyInformation?:""
                           if ( it.data?.reviews?.isNotEmpty() == true){
                               reviewsAdapter.update(it.data.reviews.toMutableList())
                               recyclerviewReviews.scheduleLayoutAnimation()
                           }else{
                               layoutReviews.hide()
                           }
                        }
                        hideProgressBar()
                    }

                    is NetworkResult.Error->{
                        hideProgressBar()
                        showToast(it.message?:"")
                    }
                }
            }
        }
    }

}