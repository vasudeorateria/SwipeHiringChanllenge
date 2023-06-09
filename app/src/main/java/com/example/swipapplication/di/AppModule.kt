package com.example.swipapplication.di

import com.example.swipapplication.model.Repository
import com.example.swipapplication.model.api.ApiInterface
import com.example.swipapplication.viewmodel.AddProductsFragmentViewModel
import com.example.swipapplication.viewmodel.ProductsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single {
        ApiInterface()
    }
    single {
        Repository(get())
    }
    viewModel {
        ProductsFragmentViewModel(get())
    }
    viewModel {
        AddProductsFragmentViewModel(get())
    }

}