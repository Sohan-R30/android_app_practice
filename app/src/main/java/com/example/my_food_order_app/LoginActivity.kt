package com.example.my_food_order_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.my_food_order_app.Model.UserModel
import com.example.my_food_order_app.databinding.ActivityLoginBinding
import com.example.my_food_order_app.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var email : String
    private lateinit var password : String
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)


        binding.loginBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.loginBtn.setOnClickListener{
            email = binding.userEmailEditTextLogin.text.toString().trim()
            password = binding.userPasswordEditTextLogin.text.toString().trim()

            if(email.isBlank() || password.isBlank())
            {
                Toast.makeText(this, "Fill All Details", Toast.LENGTH_SHORT).show()
            }
            else
            {
                loginAccount(email, password)
            }
        }
        binding.dontHaveBtnSignup.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
            result ->
        if(result.resultCode == Activity.RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful)
            {
                val account : GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                        authTask ->
                    if(authTask.isSuccessful)
                    {
                        Toast.makeText(this, "Sign In SuccessFully ", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, "Sign In Failled ${authTask.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Sign In Failed ${task.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                task ->
            if(task.isSuccessful)
            {
                Toast.makeText(this, "Sign In Successfully", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUi(user)
            }
            else
            {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        task ->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this, "Create Account Successfully", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        saveUserData()
                        updateUi(user)
                    }
                    else
                    {
                        Toast.makeText(this, "Authentication Failed ${task.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createAccount: Authentication Failed",task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.userEmailEditTextLogin.text.toString().trim()
        password = binding.userPasswordEditTextLogin.text.toString().trim()
        val user = UserModel(null,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null)
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}