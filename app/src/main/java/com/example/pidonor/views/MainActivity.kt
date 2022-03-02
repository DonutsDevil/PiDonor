package com.example.pidonor.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.example.pidonor.R
import com.example.pidonor.Utils
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
        res ->
        this.onSignInResult(res)
    }

    // Create and launch sign-in intent
    private fun createSignInIntent() {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(signInProviders())
            .setTheme(R.style.FirebaseRedTheme)
            .setLogo(R.drawable.piimage)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun signInProviders(): ArrayList<AuthUI.IdpConfig> {
        return arrayListOf(
            GoogleBuilder().build(),
            EmailBuilder().build()
        )
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        when {
            result.resultCode == RESULT_OK -> {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.email
                Log.d(TAG, "onSignInResult: ${user?.email}")
                val intent = Intent(this,HolderActivity::class.java)
                intent.putExtra(Utils.LOGGED_EMAIL_ID,user?.email)
                // pass it to another activity for handling future request
                startActivity(intent)
                finish()
            }
            response == null -> {
                // terminate the app when back button is pressed
                finish()
            }
            else -> {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}