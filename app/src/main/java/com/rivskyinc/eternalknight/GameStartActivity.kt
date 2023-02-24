package com.rivskyinc.eternalknight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rivskyinc.eternalknight.databinding.ActivityGameStartBinding

class GameStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartGame.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        binding.buttonQuit.setOnClickListener {
            this.finishAffinity()
        }

    }


    override fun onBackPressed() {

        super.onBackPressed()
        // System.runFinalizersOnExit(true)
        this.finishAffinity()
    }
}
