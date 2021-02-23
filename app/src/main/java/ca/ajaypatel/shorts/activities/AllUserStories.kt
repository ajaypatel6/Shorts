package ca.ajaypatel.shorts.activities

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.ajaypatel.shorts.R
import ca.ajaypatel.shorts.models.Story
import ca.ajaypatel.shorts.utils.DashboardAdapter
import ca.ajaypatel.shorts.utils.StoryAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_user_stories.*
import kotlinx.android.synthetic.main.story_item.*

class AllUserStories : AppCompatActivity()  {

    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser
    private val storyRef:CollectionReference = db.collection("users").document(user?.uid!!).collection("stories")
    var storyAdapter : StoryAdapter? = null
    //private var titlesList = mutableListOf<String>()
    var dashboardAdapter: DashboardAdapter? = null

    private var removedPosition: Int = 0
    private var removedItem: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user_stories)
        this.supportActionBar?.hide();

        setUpRecyclerView()

        intent.putExtra("title","")
        intent.putExtra("content", "")

    }

    // should have query, options and adapter but
    private fun setUpRecyclerView() {

        val query : Query = storyRef

        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Story> = FirestoreRecyclerOptions.Builder<Story>()
            .setQuery(query, Story::class.java)
            .build()

        // ADAPTER

        storyAdapter = StoryAdapter(firestoreRecyclerOptions);

        yourStoriesList.layoutManager = LinearLayoutManager(this)
        yourStoriesList.adapter = storyAdapter

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    intent.putExtra("title","")
                    intent.putExtra("content", "")
                    removedPosition = viewHolder.adapterPosition
                    removedItem = yourStoriesList[viewHolder.adapterPosition].toString()

                    /** //TODO: better delete, can undo, no thanks
                    Snackbar.make(viewHolder.itemView, " deleted",Snackbar.LENGTH_LONG).setAction("UNDO"){
                        //add back item
                        //
                    }.show()
                    **/

                    storyAdapter!!.deleteItem(viewHolder.adapterPosition)

                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(yourStoriesList)
        
    }
    public override fun onStart() {
        super.onStart()
        storyAdapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()
        storyAdapter!!.stopListening()
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
