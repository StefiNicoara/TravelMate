package com.example.travelmate.di

import com.example.travelmate.ui.account.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

}

val viewmodelModule = module {
    viewModel { RegisterViewModel()}
}