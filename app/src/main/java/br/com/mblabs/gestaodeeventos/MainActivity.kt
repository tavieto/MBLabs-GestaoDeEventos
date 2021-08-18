package br.com.mblabs.gestaodeeventos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.mblabs.gestaodeeventos.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        MainViewModel(activity = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        configureView()
        setClickListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.emailIsNotEmpty.observe(this, {
            if (it) {
                setEmail()
            }
        })
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.setFirebaseAuthentication()
        }
        binding.btnDB.setOnClickListener {
            viewModel.setFirebaseRealTimeDatabase()
        }
        binding.btnDBGET.setOnClickListener {
            viewModel.readDB()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun configureView() {
        val signInGoogle: TextView = binding.btnLogin.getChildAt(0) as TextView
        signInGoogle.setText(R.string.sign_in_google)

        setEmail()
    }

    private fun setEmail() {
        binding.txtEmail.text = viewModel.getEmail()
    }


}