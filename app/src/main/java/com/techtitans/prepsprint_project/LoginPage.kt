package com.techtitans.prepsprint_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        val bt_login:Button=findViewById(R.id.btnLogin)
        val google_signin_card: MaterialCardView=findViewById(R.id.google_click_view)
        val facebook_signin_card: MaterialCardView=findViewById(R.id.facebook_click_view)

        bt_login.setOnClickListener(){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        google_signin_card.setOnClickListener {
            Toast.makeText(this,"Google sign in clicked", Toast.LENGTH_LONG).show()
        }
        facebook_signin_card.setOnClickListener {
            Toast.makeText(this,"facebook sign in clicked", Toast.LENGTH_LONG).show()
        }
    }
}