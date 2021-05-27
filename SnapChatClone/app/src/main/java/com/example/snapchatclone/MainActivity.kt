package com.example.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if(mAuth.currentUser != null)
        {
            logIn()
        }

    }

    fun goClicked(view: View)
    {
        //If we can login the user
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString() , passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    //SignUp the user
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString() , passwordEditText?.text.toString())
                        .addOnCompleteListener(this){task ->
                            if (task.isSuccessful) {
                                //Add to database
                                    FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailEditText?.text.toString())
                                    Log.i("SIGNUP-->","SUCCESS!")
                                logIn()
                            } else{
                                Toast.makeText(this,"Login Failed. Try Again!",Toast.LENGTH_SHORT).show()
                                task.exception?.printStackTrace()
                            }
                        }
                }
            }



    }

    fun logIn()
    {
        //Move to next Activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

}