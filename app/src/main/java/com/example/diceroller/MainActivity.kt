package com.example.diceroller

import android.animation.Animator
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.animation.addListener

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var rollButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollButton = findViewById(R.id.button)

        rollButton.setOnClickListener {
            rollDice()
        }
    }

    fun rollDice() {
        startAnimation {
            // When animation is finished
            val diceImage: ImageView = findViewById(R.id.image_dice)
            diceImage.setImageResource(getRandomRollImage())
        }
    }

    fun getRandomRollImage(): Int {
        val dice = Dice(6)
        return when (dice.roll()) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }

    fun startAnimation(runnable: Runnable) {
        val diceImage = findViewById<ImageView>(R.id.image_dice)
        val objectAnimator = ObjectAnimator.ofFloat(diceImage, "rotation", 0f, 360f)
            .setDuration(100)

        objectAnimator.repeatCount = 7
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                rollButton.isEnabled = false

                mediaPlayer = MediaPlayer.create(this@MainActivity
                , R.raw.dice).apply {
                    isLooping = true
                    start()
                }
            }

            override fun onAnimationEnd(p0: Animator?) {
                mediaPlayer?.apply {
                    stop()
                    release()
                }

                mediaPlayer = null

                runnable.run()

                rollButton.isEnabled = true
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}

        })
        objectAnimator.start()
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}