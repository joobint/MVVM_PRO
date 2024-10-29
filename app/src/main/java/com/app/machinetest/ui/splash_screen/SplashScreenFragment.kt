package com.app.machinetest.ui.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xpayback.machinetest.R
import com.xpayback.machinetest.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment: Fragment() {
   private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){

        Glide.with(this)
            .load(R.drawable.avento)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgGif)

        lifecycleScope.launch{
            delay(3000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToProductListFragment())
        }
    }
}