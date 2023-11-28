package com.jacgr.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Inicio : AppCompatActivity() {
    private lateinit var btnRegistro: Button
    private lateinit var btnLoggeo: Button

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        btnRegistro = findViewById(R.id.Btn_ir_registro)
        btnLoggeo = findViewById(R.id.Btn_ir_loggeo)

        btnRegistro.setOnClickListener {
            startActivity(Intent(this@Inicio, RegistroActivity::class.java))
        }

        btnLoggeo.setOnClickListener {
            startActivity(Intent(this@Inicio, LoginActivity::class.java))
        }

    }

    override fun onStart() {
        comprobarSesion()
        super.onStart()
    }

    private fun comprobarSesion(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null){
            startActivity(Intent(this@Inicio, MainActivity::class.java))
            finish()
        }
    }

}