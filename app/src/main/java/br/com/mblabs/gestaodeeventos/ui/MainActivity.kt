package br.com.mblabs.gestaodeeventos.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.mblabs.gestaodeeventos.MainViewModel
import br.com.mblabs.gestaodeeventos.MainViewModelFactory
import br.com.mblabs.gestaodeeventos.R
import br.com.mblabs.gestaodeeventos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(this)
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        configureView()
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.emailIsNotEmpty.observe(this, {

        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            mainViewModel.setFirebaseAuthentication()
        }
    }

    private fun configureView() {
        val signInGoogle: TextView = binding.btnLogin.getChildAt(0) as TextView
        signInGoogle.setText(R.string.sign_in_google)
    }
}