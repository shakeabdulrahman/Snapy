package com.example.snappy.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snappy.R
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.data.model.UserDetail
import com.example.snappy.databinding.ActivitySplashBinding
import com.example.snappy.ui.MainActivity
import com.example.snappy.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private var privateSharedPrefManager: PrivateSharedPrefManager? = null

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null // auth listener
    private lateinit var db: FirebaseFirestore

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        privateSharedPrefManager = PrivateSharedPrefManager(this)

        initView()
        clickListener()
    }

    private fun clickListener() {
        binding.buttonGetStarted.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.dog_logo).setTheme(R.style.AppTheme_Actionbar)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            createUser() // creates new user
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
        }
    }

    private fun createUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userUID = currentUser?.uid!!

        val userData = UserDetail(userUID)

        db.collection("USERS").document(userUID).collection("details")
            .add(userData)
            .addOnSuccessListener {
                showMessage("User created successfully!")
            }
            .addOnFailureListener { e ->
                showMessage("User creation failed!")
            }
    }

    override fun onResume() {
        super.onResume()
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth!!.removeAuthStateListener(mAuthStateListener!!)
    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA1")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash:", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", e.toString())
        }
    }

    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}