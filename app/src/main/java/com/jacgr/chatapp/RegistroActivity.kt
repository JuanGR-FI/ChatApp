package com.jacgr.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistroActivity : AppCompatActivity() {
    private lateinit var etNombreUsuario: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepitePasswd: EditText
    private lateinit var btnRegistrar: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        //supportActionBar!!.title = "Registros"
        inicializarVariables()

        btnRegistrar.setOnClickListener {
            validarDatos()
        }

    }

    private fun inicializarVariables() {
        etNombreUsuario = findViewById(R.id.R_Et_nombre_usuario)
        etEmail = findViewById(R.id.R_Et_email)
        etPassword = findViewById(R.id.R_Et_password)
        etRepitePasswd = findViewById(R.id.R_Et_r_password)
        btnRegistrar = findViewById(R.id.Btn_registrar)

        auth = FirebaseAuth.getInstance()
    }

    private fun validarDatos() {
        val nombre = etNombreUsuario.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val r_password = etRepitePasswd.text.toString().trim()

        if (nombre.isEmpty()){
            Toast.makeText(this, "Ingrese nombre de usuario", Toast.LENGTH_SHORT).show()
        } else if(email.isEmpty()){
            Toast.makeText(this, "Ingrese su correo", Toast.LENGTH_SHORT).show()
        } else if(password.isEmpty()){
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
        } else if(r_password.isEmpty()){
            Toast.makeText(this, "Por favor repita su contraseña", Toast.LENGTH_SHORT).show()
        } else if(password != r_password){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        } else{
            registrarUsario(email, password)
        }

    }

    private fun registrarUsario(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val uid = auth.currentUser!!.uid
                    reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)

                    val hashMap = HashMap<String, Any>()
                    val h_nombre_usuario = etNombreUsuario.text.toString().trim()
                    val h_email = etEmail.text.toString().trim()

                    hashMap["uid"] = uid
                    hashMap["n_usuario"] = h_nombre_usuario
                    hashMap["email"] = h_email
                    hashMap["imagen"] = ""
                    hashMap["buscar"] = h_nombre_usuario.lowercase()

                    hashMap["nombres"] = ""
                    hashMap["apellidos"] = ""
                    hashMap["edad"] = ""
                    hashMap["profesion"] = ""
                    hashMap["domicilio"] = ""
                    hashMap["telefono"] = ""
                    hashMap["estado"] = "offline"

                    reference.updateChildren(hashMap)
                        .addOnCompleteListener { task2 ->
                            if(task2.isSuccessful){
                                Toast.makeText(this@RegistroActivity, "Se ha registrado con exito", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegistroActivity, MainActivity::class.java))

                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(this@RegistroActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                        }



                } else{
                    Toast.makeText(this@RegistroActivity, "Ha ocurrido un error!!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {  e ->
                Toast.makeText(this@RegistroActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }






    }

}