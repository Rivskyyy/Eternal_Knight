package com.rivskyinc.eternalknight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rivskyinc.eternalknight.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlayAgain.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        val getScore = intent.getIntExtra("level", 1)
        binding.textViewScore.text = getScore.toString()

    }

    override fun onBackPressed() {

        super.onBackPressed()
        // System.runFinalizersOnExit(true)
        this.finishAffinity()
    }

}