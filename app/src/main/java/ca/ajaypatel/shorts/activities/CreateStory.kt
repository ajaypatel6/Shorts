package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.ContextCompat
import ca.ajaypatel.shorts.R
import ca.ajaypatel.shorts.database.Firestore
import ca.ajaypatel.shorts.models.Story
import ca.ajaypatel.shorts.models.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_story.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_shorts_editor.*
import java.text.SimpleDateFormat
import java.util.*

class CreateStory : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_story)
        this.supportActionBar?.hide();
        val user = Firebase.auth.currentUser

        saveStoryButton.setOnClickListener() {
            // why the fuck is this not goin?
            if (editTextStoryTitle.length() >= 3) {
                vibrateDevice(this)
                Toast.makeText(
                    baseContext, "Story added",
                    Toast.LENGTH_SHORT
                ).show()

                saveStory()
                val intent = Intent(this@CreateStory, AllUserStories::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    baseContext, "Story title must be at least 3 letters long",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun saveStory() {
        val user = Firebase.auth.currentUser

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val randi = (0..100000).random()

        //vals cant be null(title,story title.. etc)
        val story = user?.let {
            Story(
                user.uid,
                editTextStoryTitle.text.toString(),
                editTextStory.text.toString(),
                currentDate,
                randomS = randi
            )
        }

        if (story != null) {
            Firestore().addStory(this, story)
            Firestore().addStoryToAllStories(this, story)
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


