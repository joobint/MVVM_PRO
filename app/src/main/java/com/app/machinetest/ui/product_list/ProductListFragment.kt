package com.app.machinetest.ui.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.machinetest.models.products.Product
import com.app.machinetest.ui.product_list.adapter.ProductAdapter
import com.app.machinetest.utils.NetworkResult
import com.app.machinetest.utils.hide
import com.app.machinetest.utils.show
import com.xpayback.machinetest.databinding.FragmentProductListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListFragment: Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private lateinit var adapter: ProductAdapter

    private val viewModel by viewModels<ProductListViewModel>(
        factoryProducer = {
            ProductListViewModelFactory()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = binding.apply {
        adapter = ProductAdapter(emptyList())
        recyclerviewProducts.adapter = adapter
        adapter.onClickOnItem = {product: Product ->
            findNavController().navigate(ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(product.id.toString()))
        }
        observeProductsList()
        viewModel.fetchProducts()
        recyclerviewProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if (!viewModel.isLockPaging){
                        if (viewModel.currentPage <= viewModel.totalPage){
                            viewModel.currentPage ++
                            viewModel.isLockPaging = true
                            viewModel.fetchProducts()
                        }
                    }
                }
            }
        })
    }

    private fun observeProductsList() {

        lifecycleScope.launch(Dispatchers.Main){

            viewModel.productsResponseLiveData.observe(viewLifecycleOwner){

                when(it){

                    is NetworkResult.Loading->{
                        showLoading()
                    }

                    is NetworkResult.Success->{
                        lifecycleScope.launch{
                            it.data?.products?.let { it1 ->
                                viewModel.list.addAll(it1)
                                adapter.updateList(viewModel.list)
                                binding.recyclerviewProducts.scheduleLayoutAnimation()
                            }
                            viewModel.totalPage = (it.data?.total ?: 0)/viewModel.perPage
                            viewModel.isLockPaging = false
                        }
                        hideLoading()
                    }

                    is NetworkResult.Error->{
                        viewModel.currentPage--
                        viewModel.isLockPaging = false
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.show()
            recyclerviewProducts.hide()
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressBar.hide()
            recyclerviewProducts.show()
        }
    }
}