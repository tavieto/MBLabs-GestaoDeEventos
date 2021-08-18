package br.com.mblabs.gestaodeeventos

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var uid: String
    private lateinit var email: String

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
            this.onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser
        setEmail(user?.email ?: "")
        setFirebaseAuthentication()
        findViewById<AppCompatButton>(R.id.btnDB).setOnClickListener {
            setFirebaseRealTimeDatabase()
        }
        findViewById<AppCompatButton>(R.id.btnDBGET).setOnClickListener {
            readDB()
        }
        findViewById<AppCompatButton>(R.id.btnLogout).setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
                setEmail()
            }
        }
    }

    private fun setFirebaseAuthentication() {
        // Choose authentication provider
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        val googleButton = findViewById<SignInButton>(R.id.btnLogin)

        val textView: TextView = googleButton.getChildAt(0) as TextView
        textView.setText(R.string.sign_in_google)

        googleButton.setOnClickListener {
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                Toast.makeText(this, "Deu certo e o e-mail do usuário é ${it.email}", Toast.LENGTH_SHORT).show()
                uid = it.uid
                email = it.email.toString()
                setEmail(email)
            }
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEmail(email: String = "") {
        findViewById<AppCompatTextView>(R.id.txtEmail).text = email
    }

    private fun setFirebaseRealTimeDatabase() {

        val database = Firebase.database
        val myRef = database.getReference(uid)
        val user = User(email, uid)

        val valueUser = Gson().toJson(user)

        Toast.makeText(this@MainActivity, "$user", Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity, "$valueUser", Toast.LENGTH_SHORT).show()
        myRef.setValue(valueUser)

        readDB()
    }

    private fun readDB() {
        val database = Firebase.database
        val myRef = database.getReference(uid)
        lateinit var json: User

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valueDB = snapshot.getValue<String>()
                valueDB?.let {
                    json = Gson().fromJson(it, User::class.java)
                }
                Toast.makeText(this@MainActivity, "Value is: ${json.email}", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to read value.", Toast.LENGTH_SHORT).show()
            }

        })
    }

}