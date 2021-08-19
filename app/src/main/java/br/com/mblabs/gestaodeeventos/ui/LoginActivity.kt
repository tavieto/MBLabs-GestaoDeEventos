package br.com.mblabs.gestaodeeventos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.mblabs.gestaodeeventos.R
import br.com.mblabs.gestaodeeventos.data.User
import br.com.mblabs.gestaodeeventos.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private val database = Firebase.database
    private lateinit var myRef: DatabaseReference

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        configureView()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnLogin.setOnClickListener {
            setFirebaseAuthentication()
        }
    }

    private fun configureView() {
        val signInGoogle: TextView = binding.btnLogin.getChildAt(0) as TextView
        signInGoogle.setText(R.string.sign_in_google)
    }

    private fun setFirebaseAuthentication() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                currentUser = it
            }
            readDB()
        }
    }

    private fun setFirebaseRealTimeDatabase() {
        currentUser?.let { firebaseUser ->
            myRef = database.getReference(firebaseUser.uid)
            val user = User(firebaseUser.email.toString(), firebaseUser.uid)
            val userJson = Gson().toJson(user)
            myRef.setValue(userJson)
        }
    }

    private fun readDB() {
        currentUser?.let { firebaseUser ->
            firebaseUser.uid.let {
                myRef = database.getReference(it)
            }
        }
        lateinit var json: User

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valueDB = snapshot.getValue<String>()
                if (valueDB.isNullOrBlank()) {
                    Timber.i("Novo cadastro")
                    setFirebaseRealTimeDatabase()
                } else {
                    Timber.i("$valueDB")
                    Timber.i("Já cadastrado.\nUID: ${currentUser?.uid}")
                }

                val dataActivity = Intent(this@LoginActivity, DataActivity::class.java)
                startActivity(dataActivity)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}