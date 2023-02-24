package com.rivskyinc.eternalknight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import com.rivskyinc.eternalknight.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var currentProgress = 0
    private var currentLevel = 1
    private var currentTimer = 4
    private var currentHealthKnight = 50
    private var currentHealthSkeleton = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animButton = AnimationUtils.loadAnimation(
            this,
            com.google.android.material.R.anim.abc_grow_fade_in_from_bottom
        )


        binding.textViewLevel.text = currentLevel.toString()
        binding.progressBarHealthSkeleton.max = currentHealthSkeleton
        binding.progressBarHealthSkeleton.progress = currentHealthSkeleton

        binding.progressBarHealthKnight.max = currentHealthKnight
        binding.progressBarHealthKnight.progress = currentHealthKnight

        startGame()


        // Start ProgressBarAttack,  PostDelayed ProgressBarAttack && buttonAttack

        Handler(Looper.getMainLooper()).postDelayed({
            val timer = object : CountDownTimer(5 * 500, 50) {
                override fun onTick(millisUntilFinished: Long) {

                    currentProgress += 1
                    binding.progressPowerAttack.progress = currentProgress

                    binding.progressPowerAttack.max = 50
                    if (currentProgress == 50) {
                        currentProgress = 0
                        binding.progressPowerAttack.progress = currentProgress
                    }

                }

                override fun onFinish() {
                    start()
                    currentProgress = 0
                    binding.progressPowerAttack.progress = currentProgress

                }

            }

            binding.buttonAttackPower.setOnClickListener {
                knightAttack()
                binding.buttonAttackPower.startAnimation(animButton)
                binding.buttonAttackPower.isEnabled = false
                if (currentProgress in 23..27) {
                    currentHealthSkeleton -= 10
                    binding.textViewHealthSkeleton.text = currentHealthSkeleton.toString()
                    binding.progressBarHealthSkeleton.progress = currentHealthSkeleton
                } else {
                    currentHealthSkeleton -= Random.nextInt(1, 3)
                    binding.textViewHealthSkeleton.text = currentHealthSkeleton.toString()
                    binding.progressBarHealthSkeleton.progress = currentHealthSkeleton
                }
                if (currentHealthSkeleton >= 1) {

                    Handler().postDelayed({
                        skeletonAttack()
                    }, 1000)
                }

                nextLevel()
            }

            if (currentProgress == 0) {
                timer.start()

            }

        }, 3000)

        gameOver()

    }


    private fun startGame() {
        val timerStartGame = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {


                currentTimer -= 1
                binding.textViewTimerStartGame.text = currentTimer.toString()
                if (currentTimer == 0) {
                    binding.textViewTimerStartGame.text = "FIGHT!"
                }


            }

            override fun onFinish() {
                if (currentTimer == 0) {
                    binding.textViewTimerStartGame.visibility = View.GONE
                }
            }

        }
        timerStartGame.start()
    }

    private fun knightAttack() {
        val animKnight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right)
        binding.imageKnight.startAnimation(animKnight)
        binding.explosionSkeleton.postDelayed({
            binding.explosionSkeleton.visibility = View.VISIBLE
        }, 300)

        binding.explosionSkeleton.postDelayed({
            binding.explosionSkeleton.visibility = View.GONE
        }, 600)


    }

    private fun skeletonAttack() {
        val animSkeleton = AnimationUtils.loadAnimation(this, R.anim.from_right_to_left)
        if (currentHealthSkeleton >= 1) {
            binding.imageSkeleton.startAnimation(animSkeleton)
            currentHealthKnight -= Random.nextInt(1, 5)
            binding.textViewHealthKnight.text = currentHealthKnight.toString()
            binding.progressBarHealthKnight.progress = currentHealthKnight
            binding.explosionKnight.postDelayed({
                binding.explosionKnight.visibility = View.VISIBLE
            }, 300)

            binding.explosionKnight.postDelayed({
                binding.explosionKnight.visibility = View.GONE
                binding.buttonAttackPower.isEnabled = true
            }, 600)
            gameOver()
        }


    }

    private fun nextLevel() {
        if (currentHealthSkeleton <= 0) {

            binding.textViewLevelUp.visibility = View.VISIBLE
            binding.textViewLevelUp.postDelayed({
                binding.textViewLevelUp.visibility = View.GONE
            }, 2000)
            currentLevel += 1
            binding.textViewLevel.text = currentLevel.toString()

            val starthealthSkeleton = 15
            currentHealthSkeleton = starthealthSkeleton + currentLevel
            binding.textViewHealthSkeleton.text = currentHealthSkeleton.toString()
            binding.progressBarHealthSkeleton.progress = currentHealthSkeleton
            binding.progressBarHealthSkeleton.max = currentHealthSkeleton

            binding.imageSkeleton.postDelayed({
                binding.imageSkeleton.visibility = View.GONE
            }, 300)
            binding.imageSkeleton.postDelayed({
                binding.imageSkeleton.visibility = View.VISIBLE
            }, 3000)
            binding.grave.postDelayed({
                binding.grave.visibility = View.VISIBLE
            }, 300)
            binding.grave.postDelayed({
                binding.grave.visibility = View.GONE
                binding.buttonAttackPower.isEnabled = true
            }, 3001)
        }
    }

    private fun gameOver() {
        if (currentHealthKnight <= 1) {
            val intent = Intent(this, GameOverActivity::class.java)
            intent.putExtra("level", currentLevel)

            startActivity(intent)

        }
    }
}
