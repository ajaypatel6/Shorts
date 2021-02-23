package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import ca.ajaypatel.shorts.R
import ca.ajaypatel.shorts.database.Firestore
import ca.ajaypatel.shorts.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.supportActionBar?.hide();

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener() {
            vibrateDevice(this)
            registerUser()
        }
    }

    private fun registerUser() {
        if (editTextUsername.text.toString().isEmpty()) {
            editTextUsername.error = "Enter username"
            editTextUsername.requestFocus()
            return
        }

        if (editTextEmail.text.toString().isEmpty()) {
            editTextEmail.error = "Enter Email"
            editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()) {
            editTextEmail.error = "Enter valid email"
            editTextEmail.requestFocus()
            return
        }

        if (editTextPassword.text.toString().isEmpty()) {
            editTextPassword.error = "Enter password"
            editTextPassword.requestFocus()
            return
        }

        if (editTextReenterPassword.text.toString().isEmpty()) {
            editTextReenterPassword.error = "Re-enter password"
            editTextReenterPassword.requestFocus()
            return
        }

        if (!editTextPassword.text.toString().equals(editTextReenterPassword.text.toString())) {
            editTextReenterPassword.error = "Passwords do not match"
            editTextReenterPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(
            editTextEmail.text.toString(),
            editTextPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val randy = (0..100000).random()

                    val user = User(
                        firebaseUser.uid,
                        editTextEmail.text.toString(),
                        editTextUsername.text.toString(),
                        random = randy
                    )

                    //got info, now need to store in cloud
                    Firestore().addUser(this, user)

                    //Firestore().addUserExample(this)

                    Toast.makeText(
                        baseContext, "Registration completed, please sign in.",
                        Toast.LENGTH_SHORT
                    ).show()

                    //contains info

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    //val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("tag", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Registration failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }

                // ...
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