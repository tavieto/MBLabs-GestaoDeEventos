package br.com.mblabs.gestaodeeventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.mblabs.gestaodeeventos.ui.LoginActivity

class LoginViewModelFactory(private val arg: LoginActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LoginActivity::class.java).newInstance(arg)
    }

}