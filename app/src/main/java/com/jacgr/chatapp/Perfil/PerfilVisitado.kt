package com.jacgr.chatapp.Perfil

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jacgr.chatapp.Modelo.Usuario
import com.jacgr.chatapp.R

class PerfilVisitado : AppCompatActivity() {

    private lateinit var PV_ImagenU: ImageView

    private lateinit var PV_NombreU: TextView
    private lateinit var PV_EmailU: TextView
    private lateinit var PV_Uid: TextView
    private lateinit var PV_nombres: TextView
    private lateinit var PV_apellidos: TextView
    private lateinit var PV_profesion: TextView
    private lateinit var PV_telefono: TextView
    private lateinit var PV_edad: TextView
    private lateinit var PV_domicilio: TextView
    private lateinit var PV_proveedor: TextView
    private lateinit var Btn_llamar: Button
    private lateinit var Btn_enviar_sms: Button

    var uid_usuario_visitado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_visitado)
        inicializarVistas()
        obtenerUid()
        leerInformacionUsuario()

        Btn_llamar.setOnClickListener {
            if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                realizarLlamada()
            }else{
                requestCallPhonePermiso.launch(Manifest.permission.CALL_PHONE)
            }
        }

        Btn_enviar_sms.setOnClickListener {
            if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                enviarSms()
            }else{
                requestSendMessagePermiso.launch(Manifest.permission.SEND_SMS)
            }
        }

    }

    private fun inicializarVistas() {
        PV_ImagenU = findViewById(R.id.PV_ImagenU)

        PV_NombreU = findViewById(R.id.PV_NombreU)
        PV_EmailU = findViewById(R.id.PV_EmailU)
        PV_Uid = findViewById(R.id.PV_Uid)
        PV_nombres = findViewById(R.id.PV_nombres)
        PV_apellidos = findViewById(R.id.PV_apellidos)
        PV_profesion = findViewById(R.id.PV_profesion)
        PV_telefono = findViewById(R.id.PV_telefono)
        PV_edad = findViewById(R.id.PV_edad)
        PV_domicilio = findViewById(R.id.PV_domicilio)
        PV_proveedor = findViewById(R.id.PV_proveedor)

        Btn_llamar = findViewById(R.id.Btn_llamar)
        Btn_enviar_sms = findViewById(R.id.Btn_enviar_sms)
    }

    private fun obtenerUid() {
        intent = intent
        uid_usuario_visitado = intent.getStringExtra("uid").toString()
    }

    private fun leerInformacionUsuario() {
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid_usuario_visitado)

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
                //Obtener la informacion en tiempo real
                PV_NombreU.text = usuario!!.getN_Usuario()
                PV_EmailU.text = usuario!!.getEmail()
                PV_Uid.text = usuario!!.getUid()
                PV_nombres.text = usuario!!.getNombres()
                PV_apellidos.text = usuario!!.getApellidos()
                PV_profesion.text = usuario!!.getProfesion()
                PV_telefono.text = usuario!!.getTelefono()
                PV_edad.text = usuario!!.getEdad()
                PV_domicilio.text = usuario!!.getDomicilio()
                PV_proveedor.text = usuario!!.getProveedor()

                Glide.with(applicationContext).load(usuario.getImagen())
                    .placeholder(R.drawable.imagen_usuario_visitado).into(PV_ImagenU)

            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })

    }

    private fun realizarLlamada() {
        val numeroUsuario = PV_telefono.text.toString()
        if(numeroUsuario.isEmpty()){
            Toast.makeText(this, "El usuario no cuenta con un numero telefonico", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$numeroUsuario")
            startActivity(intent)
        }
    }

    private fun enviarSms() {
        val numeroUsuario = PV_telefono.text.toString()
        if(numeroUsuario.isEmpty()){
            Toast.makeText(this, "El usuario no cuenta con un numero telefonico", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:$numeroUsuario")
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }
    }

    private val requestCallPhonePermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ Permiso_concedido ->
            if(Permiso_concedido){
                realizarLlamada()
            }else{
                Toast.makeText(this, "El permiso de realizar llamadas telefonicas no ha sido concedido", Toast.LENGTH_SHORT).show()
            }
    }

    private val requestSendMessagePermiso =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ Permiso_concedido ->
            if(Permiso_concedido){
                enviarSms()
            }else{
                Toast.makeText(this, "El permiso de enviar SMS no ha sido concedido", Toast.LENGTH_SHORT).show()
            }
        }



}