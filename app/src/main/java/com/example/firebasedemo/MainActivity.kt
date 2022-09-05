package com.example.firebasedemo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebasedemo.chat.ChatActivity
import com.example.firebasedemo.chatusers.ChatUserListActivity
import com.example.firebasedemo.service.FcmService
import com.example.firebasedemo.users.UserListActivity
import com.firebase.ui.auth.viewmodel.RequestCodes
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val remoteConfig = Firebase.remoteConfig
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val googleCredential = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val idToken = googleCredential.result.idToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with Firebase.
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("CHECK", "signInWithCredential:success")
                                val user = auth.currentUser
                                findViewById<TextView>(R.id.mailTV).text = user?.email
                                if (user != null) {
                                    startActivity(Intent(applicationContext,ChatUserListActivity::class.java))
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e("CHECK", "signInWithCredential:failure", task.exception)
                            }
                        }
                }
                else -> {
                    // Shouldn't happen.
                    Log.d("CHECK", "No ID token!")
                }
            }
        } else {
            Log.e("CHECK", "Error")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkIsRegister()
        setUpRemoteConfig()
        fetchRemoteConfigData()
        setUpGoogleSignIn()
    }

    private fun checkIsRegister() {

    }

    private fun setUpRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_default)

    }

    private fun fetchRemoteConfigData() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val text = remoteConfig.getString("mainActivityText")

                    findViewById<TextView>(R.id.remoteConfigText).text = text
                }
            }
    }

    private fun setUpGoogleSignIn() {
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<Button>(R.id.googleBtn).setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

}