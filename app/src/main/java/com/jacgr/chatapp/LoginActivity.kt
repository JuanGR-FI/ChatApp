package com.jacgr.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var TXT_ir_registro: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //supportActionBar!!.title = "Login"
        inicializarVariables()

        btnLogin.setOnClickListener {
            validarDatos()
        }

        TXT_ir_registro.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistroActivity::class.java))
        }

    }

    private fun inicializarVariables() {
        etEmail = findViewById(R.id.L_Et_email)
        etPassword = findViewById(R.id.L_Et_password)
        btnLogin = findViewById(R.id.Btn_login)
        TXT_ir_registro = findViewById(R.id.TXT_ir_registro)

        auth = FirebaseAuth.getInstance()
    }

    private fun validarDatos() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if(email.isEmpty()){
            Toast.makeText(this@LoginActivity, "Ingrese su correo electronico", Toast.LENGTH_SHORT).show()
        }

        if(password.isEmpty()){
            Toast.makeText(this@LoginActivity, "Ingrese su contrasena", Toast.LENGTH_SHORT).show()
        }

        else{
            loginUsuario(email, password)
        }

    }

    private fun loginUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else{
                    Toast.makeText(this@LoginActivity, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener { e ->
                Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}