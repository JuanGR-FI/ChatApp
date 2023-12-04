package com.jacgr.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jacgr.chatapp.Fragmentos.FragmentoChats
import com.jacgr.chatapp.Fragmentos.FragmentoUsuarios
import com.jacgr.chatapp.Modelo.Chat
import com.jacgr.chatapp.Modelo.Usuario
import com.jacgr.chatapp.Perfil.PerfilActivity

class MainActivity : AppCompatActivity() {

    var reference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private lateinit var nombre_usuario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarComponentes()
        obtenerDato()
    }

    fun inicializarComponentes() {

        val toolbar: Toolbar = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(firebaseUser!!.uid)

        nombre_usuario = findViewById(R.id.Nombre_Usuario)

        val tabLayout: TabLayout = findViewById(R.id.TabLayoutMain)
        val viewPager: ViewPager = findViewById(R.id.ViewPagerMain)

        /*val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewpagerAdapter.addItem(FragmentoUsuarios(), "Usuarios")
        viewpagerAdapter.addItem(FragmentoChats(), "Chats")

        viewPager.adapter = viewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)*/

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)
                var contMensajesNoLeidos = 0
                for(dataSnapshot in snapshot.children){
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if(chat!!.getReceptor().equals(firebaseUser!!.uid) && !chat.isVisto()){
                        contMensajesNoLeidos++
                    }
                }
                if(contMensajesNoLeidos == 0){
                    viewpagerAdapter.addItem(FragmentoChats(), "Chats")
                }else{
                    viewpagerAdapter.addItem(FragmentoChats(), "[$contMensajesNoLeidos]Chats")
                }
                viewpagerAdapter.addItem(FragmentoUsuarios(), "Usuarios")
                viewPager.adapter = viewpagerAdapter
                tabLayout.setupWithViewPager(viewPager)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun obtenerDato() {
        reference!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
                    nombre_usuario.text = usuario!!.getN_Usuario()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){

        private val listaFragmentos: MutableList<Fragment> = ArrayList()
        private val listaTitulos: MutableList<String> = ArrayList()
        override fun getCount(): Int {
            return listaFragmentos.size
        }

        override fun getItem(position: Int): Fragment {
            return listaFragmentos[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listaTitulos[position]
        }

        fun addItem(fragment: Fragment, titulo: String) {
            listaFragmentos.add(fragment)
            listaTitulos.add(titulo)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_perfil -> {
                startActivity(Intent(this@MainActivity, PerfilActivity::class.java))
                return true
            }
            R.id.menu_acerca_de -> {
                Toast.makeText(this@MainActivity, "Acerca de", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_salir -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, Inicio::class.java))
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}