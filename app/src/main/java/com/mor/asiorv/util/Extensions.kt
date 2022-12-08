package com.mor.asiorv.util

import android.view.View

fun View.visiBool(isVisible: Boolean){
    visibility = if (isVisible){
        View.VISIBLE
    }else{
        View.GONE
    }
}
