package com.dreavee.test1.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dreavee.test1.api.ProductViewModelOld
import com.dreavee.test1.api.ApiHelper
import com.dreavee.test1.room.DatabaseHelper

class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProductViewModelOld::class.java)) {
            return ProductViewModelOld(apiHelper, dbHelper) as T
        }





        throw IllegalArgumentException("Unknown class name")
    }



}