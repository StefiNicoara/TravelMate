package com.example.travelmate.di

import com.example.travelmate.repository.UserRepository
import com.example.travelmate.ui.account.login.LoginViewModel
import com.example.travelmate.ui.account.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { UserRepository() }
}

val viewModelModule = module {
    viewModel { RegisterViewModel(get())}
    viewModel { LoginViewModel(get()) }
}