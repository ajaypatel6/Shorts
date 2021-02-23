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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

private lateinit var auth: FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val emailAddress = resetEmail

        this.supportActionBar?.hide();

        auth = FirebaseAuth.getInstance()

        sendPasswordReset.setOnClickListener() {
            vibrateDevice(this)
            auth.sendPasswordResetEmail(emailAddress.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_LONG).show()
                        Log.d("RESET", "Email sent to ${emailAddress.text.toString()}.")
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Log.d("RESET", "Email NOT sent")
                        Toast.makeText(this, "Email not sent", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


}
