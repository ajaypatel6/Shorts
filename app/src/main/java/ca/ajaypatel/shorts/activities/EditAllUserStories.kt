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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_story.*
import kotlinx.android.synthetic.main.activity_create_story.editTextStory
import kotlinx.android.synthetic.main.activity_create_story.editTextStoryTitle
import kotlinx.android.synthetic.main.activity_create_story.saveStoryButton
import kotlinx.android.synthetic.main.activity_edit_all_user_stories.*
import kotlinx.android.synthetic.main.story_item.*
import java.text.SimpleDateFormat
import java.util.*


class EditAllUserStories : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_all_user_stories)
        this.supportActionBar?.hide();


        val title = intent.getStringExtra("title")
        editorTextStoryTitle.setText(title)

        val content = intent.getStringExtra("content")
        editorTextStory.setText(content)

        //intent.data = null;
        intent.putExtra("title", "")
        intent.putExtra("content", "")

        saveStoryButton.setOnClickListener() {
            if (editorTextStoryTitle.length() >= 3) {
                vibrateDevice(this)
                saveStory()
                Toast.makeText(
                    baseContext, "{$title} updated",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@EditAllUserStories, AllUserStories::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    baseContext, "Title should be 3 letters long",
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

        val uid = user?.uid

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val randyS = (0..100000).random()


        //vals cant be null(title,story title.. etc)
        val editedStory = user?.let {
            Story(
                it.uid,
                editorTextStoryTitle.text.toString(),
                editorTextStory.text.toString(),
                currentDate,
                randomS = randyS

            )
        }

        if (editedStory != null) {
            Firestore().addEditedStory(this, editedStory)
            Firestore().addStoryToAllStories(this, editedStory)
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
