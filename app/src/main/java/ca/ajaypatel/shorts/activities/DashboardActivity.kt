package ca.ajaypatel.shorts.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ca.ajaypatel.shorts.R
import ca.ajaypatel.shorts.models.Story
import ca.ajaypatel.shorts.utils.DashboardAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_user_stories.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_register.*

class DashboardActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser

    private val storyRef: CollectionReference =
        db.collection("users").document(user?.uid!!).collection("stories")

    private var usersRef : CollectionReference = db.collection("users")



    var dashboardAdapter: DashboardAdapter? = null
    //private var titlesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setUpRecyclerView()

        this.supportActionBar?.hide();

        profileButton.setOnClickListener() {
            vibrateDevice(this)
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        shortButton.setOnClickListener() {
            vibrateDevice(this)
            startActivity(Intent(this, ShortsEditor::class.java))
        }

    }

    private fun setUpRecyclerView() {
        val randi = (0..100000).random()

        //usersRef.whereGreaterThan("random",randi).orderBy("random").limit(3)
        //usersRef.document().collection("stories")
        //usersRef.whereGreaterThan("randomS", randi).orderBy("randomS").limit(3)


        val randomRef = db.collection("AllStories")
        //val query: Query = randomRef.whereLessThanOrEqualTo("random", 50000).
        //val query: Query = user?.let { randomRef.document(it.uid) }!!.collection("stories").whereLessThan("randomS",50000)
        val query : Query = randomRef.whereGreaterThan("randomS",randi)
            .orderBy("randomS")
            .limit(10)

        //val storyRef:CollectionReference = db.collection("users")
          //  .document(user?.uid!!)
            //.collection("stories")

        //val query: Query = storyRef

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Story> =
            FirestoreRecyclerOptions.Builder<Story>()
                .setQuery(query, Story::class.java)
                .build()

        // ADAPTER

        dashboardAdapter = DashboardAdapter(firestoreRecyclerOptions);

        dashboardView.layoutManager = LinearLayoutManager(this)
        dashboardView.adapter = dashboardAdapter

    }


    public override fun onStart() {
        super.onStart()
        dashboardAdapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()
        dashboardAdapter!!.stopListening()
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