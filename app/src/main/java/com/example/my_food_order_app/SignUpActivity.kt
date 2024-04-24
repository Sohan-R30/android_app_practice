package com.example.my_food_order_app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.my_food_order_app.Model.UserModel
import com.example.my_food_order_app.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var email : String
    private lateinit var password : String
    private lateinit var userName : String
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient


    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)


        binding.googleLoginBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }


        binding.createAccountBtn.setOnClickListener {
            userName = binding.userNameEditText.text.toString()
            email = binding.userEmailEditText.text.toString().trim()
            password = binding.userPasswordEditText.text.toString().trim()

            if(email.isBlank() || userName.isBlank() || password.isBlank())
            {
                Toast.makeText(this, "Fill All Details", Toast.LENGTH_SHORT).show()
            }
            else
            {
                createAccount(email, password)
            }

        }

        binding.alreadyHaveBtnLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
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
                val account : GoogleSignInAccount ? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                        authTask ->
                    if(authTask.isSuccessful)
                    {
                        Toast.makeText(this, "Sign Up SuccessFully ", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, "Sign Up Failled ${authTask.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this, "Sign Up Failed ${task.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this, "Failed ${task.exception?.message.toString()}", Toast.LENGTH_SHORT).show()
                Log.d("Account", "Error : ${task.exception}")
            }
        }
    }

    private fun saveUserData() {
        userName = binding.userNameEditText.text.toString()
        email = binding.userEmailEditText.text.toString().trim()
        password = binding.userPasswordEditText.text.toString().trim()

        val user = UserModel(userName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }
}