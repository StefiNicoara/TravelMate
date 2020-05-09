package com.example.travelmate.ui.account.register

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {

    var nickname: ObservableField<String> = ObservableField()
    var email: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    var confirmPassword: ObservableField<String> = ObservableField()


}