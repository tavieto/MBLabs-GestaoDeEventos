package br.com.mblabs.gestaodeeventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.mblabs.gestaodeeventos.ui.MainActivity

class MainViewModelFactory(private val arg: MainActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MainActivity::class.java).newInstance(arg)
    }

}