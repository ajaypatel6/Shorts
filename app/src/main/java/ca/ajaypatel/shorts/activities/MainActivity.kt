package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import ca.ajaypatel.shorts.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

private val vibrate: Vibrator? = null

private var auth: FirebaseAuth = FirebaseAuth.getInstance()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.supportActionBar?.hide();

        loginButton.setOnClickListener() {
            vibrateDevice(this)
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signupButton.setOnClickListener() {
            vibrateDevice(this)
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }

    public override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser

        if(user!=null){
            // there is a user
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

    }

}

fun vibrateDevice(context: Context) {
    val vibrator = getSystemService(context, Vibrator::class.java)
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= 26) {
            it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(100)
        }
    }
}

