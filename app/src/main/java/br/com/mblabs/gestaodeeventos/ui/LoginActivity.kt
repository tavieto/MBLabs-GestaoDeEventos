package br.com.mblabs.gestaodeeventos.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.mblabs.gestaodeeventos.LoginViewModel
import br.com.mblabs.gestaodeeventos.LoginViewModelFactory
import br.com.mblabs.gestaodeeventos.R
import br.com.mblabs.gestaodeeventos.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginViewModelFactory(this)
        ).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        configureView()
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        loginViewModel.emailIsNotEmpty.observe(this, {

        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.setFirebaseAuthentication()
        }
    }

    private fun configureView() {
        val signInGoogle: TextView = binding.btnLogin.getChildAt(0) as TextView
        signInGoogle.setText(R.string.sign_in_google)
    }
}