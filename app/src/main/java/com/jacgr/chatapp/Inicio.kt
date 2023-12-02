package com.jacgr.chatapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class Inicio : AppCompatActivity() {
    private lateinit var btnLoggeo: MaterialButton
    private lateinit var btnLoggeoGoogle: MaterialButton

    var firebaseUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        btnLoggeo = findViewById(R.id.Btn_ir_loggeo)
        btnLoggeoGoogle = findViewById(R.id.BTN_login_google)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        btnLoggeo.setOnClickListener {
            startActivity(Intent(this@Inicio, LoginActivity::class.java))
        }

        btnLoggeoGoogle.setOnClickListener {
            empezarInicioSesionGoogle()
        }

    }

    private fun empezarInicioSesionGoogle() {
        val googleSignIntent = mGoogleSignInClient.signInIntent
        googleSignInARL.launch(googleSignIntent)
    }

    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { resultado ->
        if(resultado.resultCode == RESULT_OK){
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                authenticationGoogleFirebase(account.idToken)
            } catch(e: Exception){
                Toast.makeText(this, "Ha ocurrido una excepcion debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun authenticationGoogleFirebase(idToken: String?) {
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credencial)
            .addOnSuccessListener { authResult ->
                if (authResult.additionalUserInfo!!.isNewUser) {
                    guardarInfoBD()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun guardarInfoBD() {
        progressDialog.setMessage("Se esta registrando su informacion")
        progressDialog.show()

        //Obtener informacion de la cuenta de Google
        val uidGoogle = auth.uid
        val correoGoogle = auth.currentUser?.email
        val n_Google = auth.currentUser?.displayName
        val nombre_usuario_G: String = n_Google.toString()

        val hashMap = HashMap<String, Any?>()

        hashMap["uid"] = uidGoogle
        hashMap["n_usuario"] = nombre_usuario_G
        hashMap["email"] = correoGoogle
        hashMap["imagen"] = ""
        hashMap["buscar"] = nombre_usuario_G.lowercase()

        hashMap["nombres"] = ""
        hashMap["apellidos"] = ""
        hashMap["edad"] = ""
        hashMap["profesion"] = ""
        hashMap["domicilio"] = ""
        hashMap["telefono"] = ""
        hashMap["estado"] = "offline"
        hashMap["proveedor"] = "Google"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uidGoogle!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
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