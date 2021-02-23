package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import ca.ajaypatel.shorts.R
import kotlinx.android.synthetic.main.activity_shorts_editor.*

class ShortsEditor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorts_editor)

        this.supportActionBar?.hide();

        addStoryButton.setOnClickListener(){
            vibrateDevice(this)
            startActivity(Intent(this, CreateStory::class.java))

        //finish()
        }

        viewStoryButton.setOnClickListener(){
            vibrateDevice(this)
            startActivity(Intent(this, AllUserStories::class.java))
        }

    }
    private fun vibrateDevice(context: Context) {
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= 26) {
                it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(100)
            }
        }
    }
}