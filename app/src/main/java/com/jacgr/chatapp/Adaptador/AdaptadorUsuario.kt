package com.jacgr.chatapp.Adaptador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jacgr.chatapp.Modelo.Usuario
import com.jacgr.chatapp.R

class AdaptadorUsuario(context: Context, listaUsuarios: List<Usuario>): RecyclerView.Adapter<AdaptadorUsuario.ViewHolder>() {

    private val context: Context
    private val listaUsuarios: List<Usuario>

    init {
        this.context = context
        this.listaUsuarios = listaUsuarios
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nombreUsuario: TextView
        var emailUsuario: TextView
        var imagenUsuario: ImageView

        init {
            nombreUsuario = itemView.findViewById(R.id.Item_nombre_usuario)
            emailUsuario = itemView.findViewById(R.id.Item_email_usuario)
            imagenUsuario = itemView.findViewById(R.id.Item_imagen)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario: Usuario = listaUsuarios[position]

        holder.nombreUsuario.text = usuario.getN_Usuario()
        holder.emailUsuario.text = usuario.getEmail()

        Glide.with(context)
            .load(usuario.getImagen())
            .placeholder(R.drawable.ic_item_usuario)
            .into(holder.imagenUsuario)

    }


}