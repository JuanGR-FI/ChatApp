package com.jacgr.chatapp.Perfil

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.jacgr.chatapp.R

class EditarImagenPerfil : AppCompatActivity() {
    private lateinit var BtnElegirImagen: Button
    private lateinit var BtnActualizarImagen: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_imagen_perfil)

        BtnElegirImagen = findViewById(R.id.BtnElegirImagenDe)
        BtnActualizarImagen = findViewById(R.id.BtnActualizarImagen)

        BtnElegirImagen.setOnClickListener {
            Toast.makeText(this, "Seleccionar imagen de", Toast.LENGTH_SHORT).show()
            mostrarDialogo()
        }

        BtnActualizarImagen.setOnClickListener {
            Toast.makeText(this, "Actualizar imagen", Toast.LENGTH_SHORT).show()
        }

    }

    private fun mostrarDialogo() {
        val Btn_abrir_galeria: Button
        val Btn_abrir_camara: Button

        val dialog = Dialog(this@EditarImagenPerfil)

        dialog.setContentView(R.layout.cuadro_d_seleccionar)

        Btn_abrir_galeria = dialog.findViewById(R.id.Btn_abrir_galeria)
        Btn_abrir_camara = dialog.findViewById(R.id.Btn_abrir_camara)

        Btn_abrir_galeria.setOnClickListener {
            Toast.makeText(this, "Abrir galeria", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        Btn_abrir_camara.setOnClickListener {
            Toast.makeText(this, "Abrir camara", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()

    }

}