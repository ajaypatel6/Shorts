package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ca.ajaypatel.shorts.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object{
        private const val RC_SIGN_IN = 12000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.supportActionBar?.hide();

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //initialize firebase auth
        auth = FirebaseAuth.getInstance()
        val user = Firebase.auth.currentUser

        if(user!=null){
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        googleButton.setOnClickListener{
            vibrateDevice(this)
            signIn()
        }

        forgotPasswordTxt.setOnClickListener(){
            vibrateDevice(this)
            startActivity(Intent(this, ForgotPassword::class.java))
            finish()
        }

        // take you to register activity
        signupButton.setOnClickListener(){
            vibrateDevice(this)
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener(){
            vibrateDevice(this)
            doLogin();
            finish()
        }

        //TODO: google and facebook? 3rd
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SIGNGINtry", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SINGINcatch", "Google sign in failed", e)
                    // ...
                }
            } else {
                Log.w("SING IN ELSE",exception.toString())

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNIN", "signInWithCredential:success")
                    val user = Firebase.auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    //finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGNIN", "signInWithCredential:failure", task.exception)
                }
            }
    }


    private fun doLogin() {

        if (loginEditTextEmail.text.toString().isEmpty()) {
            loginEditTextEmail.error = "Enter Email"
            loginEditTextEmail.requestFocus()
            return
        }

        if (loginEditTextPassword.text.toString().isEmpty()) {
            loginEditTextPassword.error = "Enter password"
            loginEditTextPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(
            loginEditTextEmail.text.toString(),
            loginEditTextPassword.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Log in is failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        //val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    // taking nullable firebase user
    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else{
            Toast.makeText(
                baseContext, "Log in is failed.",
                Toast.LENGTH_SHORT
            ).show()
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
