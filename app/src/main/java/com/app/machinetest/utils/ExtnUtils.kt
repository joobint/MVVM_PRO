package com.app.machinetest.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun Fragment.showToast(msg: String){
   Toast.makeText(this.requireContext(),msg,Toast.LENGTH_LONG).show()
}