package com.jacgr.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mostrarBienvenida()
    }

    fun mostrarBienvenida() {
        object: CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
                //TODO("Not yet implemented")
            }

            override fun onFinish() {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }

        }.start()
    }

}