package com.projet2.venteplantes

import android.app.Activity
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.projet2.venteplantes.databinding.ActivityMainBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    //constants
   private companion object{
       private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.login_layout)
        var btn = findViewById<SignInButton>(R.id.googleSignInBtn)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Google SignIn Button
        btn.setOnClickListener{
            Log.d(TAG,"onCreate: commencer google signin")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            Log.d(TAG,"intent results")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e:Exception){
                //echouee
                Log.d(TAG,"hjj${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "dsfezqfcez")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->

                Log.d(TAG, "gibberish")

                val firebaseUser = firebaseAuth.currentUser

                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email
                Log.d(TAG, "Uid $uid")
                Log.d(TAG, "Uid $email")

                if(authResult.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "dfklsj")
                    Toast.makeText(this@LoginActivity,"Compte crée", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this@LoginActivity,"inscrivé en tant que $email", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this@LoginActivity,"Echouee", Toast.LENGTH_SHORT).show()
            }
        Log.d(TAG, "dsfezqfcez")

    }
}