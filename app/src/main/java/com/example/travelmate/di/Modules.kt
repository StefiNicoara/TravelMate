package com.example.travelmate.di

import com.example.travelmate.repository.AttractionsRepository
import com.example.travelmate.repository.UserAccountRepository
import com.example.travelmate.ui.account.login.LoginViewModel
import com.example.travelmate.ui.account.register.RegisterViewModel
import com.example.travelmate.ui.addAttraction.AddAttractionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { UserAccountRepository() }
    single { AttractionsRepository() }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { AddAttractionViewModel(get()) }
}