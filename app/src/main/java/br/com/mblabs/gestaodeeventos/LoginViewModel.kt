package br.com.mblabs.gestaodeeventos

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.mblabs.gestaodeeventos.data.User
import br.com.mblabs.gestaodeeventos.ui.LoginActivity
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

class LoginViewModel(
    private val activity: LoginActivity
): ViewModel() {

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private val database = Firebase.database
    private lateinit var myRef: DatabaseReference

    private val _emailIsNotEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val emailIsNotEmpty: LiveData<Boolean> = _emailIsNotEmpty

    private val signInLauncher = activity.registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        onSignInResult(res)
    }

    fun setFirebaseAuthentication() {
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
            _emailIsNotEmpty.value = true
        }
    }

    fun setFirebaseRealTimeDatabase() {
        currentUser?.let { firebaseUser ->
            myRef = database.getReference(firebaseUser.uid)
            val user = User(firebaseUser.email.toString(), firebaseUser.uid)
            val userJson = Gson().toJson(user)
            myRef.setValue(userJson)
        }
    }

    fun readDB() {
        currentUser?.let { firebaseUser ->
            firebaseUser.uid.let {
                myRef = database.getReference(it)
            }
        }
        lateinit var json: User

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valueDB = snapshot.getValue<String>()
                valueDB?.let {
                    json = Gson().fromJson(it, User::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun logout() {
        AuthUI.getInstance().signOut(activity).addOnCompleteListener {
            _emailIsNotEmpty.value = false
        }
    }

    fun getEmail(): String {
        return currentUser?.email ?: ""
    }
}