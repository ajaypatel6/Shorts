package ca.ajaypatel.shorts.utils

import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import ca.ajaypatel.shorts.R
import ca.ajaypatel.shorts.activities.AllUserStories
import ca.ajaypatel.shorts.activities.EditAllUserStories
import ca.ajaypatel.shorts.models.Story
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.story_item.*
import kotlinx.android.synthetic.main.story_item.view.*


//Finished Adapter
// CONVERTED java - kotlin with IDE
class StoryAdapter(options: FirestoreRecyclerOptions<Story>) :
    FirestoreRecyclerAdapter<Story, StoryAdapter.StoryHolder>(
        options
    ) {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See [ ] for configuration options.
     *
     * @param options
     */

    private val db = FirebaseFirestore.getInstance()
    val user = Firebase.auth.currentUser

    private val storyRef: CollectionReference =
        db.collection("users").document(user?.uid!!).collection("stories")

    override fun onBindViewHolder(holder: StoryHolder, position: Int, model: Story) {
        holder.story_title.text = model.title
        holder.story_content.text = model.story
        holder.story_date.text = model.date
    }

    // which layout it has to inflate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryHolder {
        return StoryHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.story_item,
                parent,
                false
            )
        )
    }

    // delete document from particular position
    fun deleteItem(position: Int) {
        // delete form user
        snapshots.getSnapshot(position).reference.delete()

        //delete from allstories
        val id = user!!.uid + "" + snapshots.get(position).title.substring(0,3)
        db.collection("AllStories").document(id)
            .delete()
    }

    inner class StoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var story_title = itemView.story_title
        var story_date = itemView.story_date
        var story_content = itemView.story_content


        //Log.d("MSG", "you clicked on ${story_title.text}")


        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition

                //Toast.makeText(itemView.context, "you clicked on ${story_title.text} at position ${position+1}",Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context, EditAllUserStories::class.java)
                intent.putExtra("title", "")
                intent.putExtra("content", "")

                intent.putExtra("title", story_title.text)
                intent.putExtra("content", story_content.text)

                itemView.context.startActivity(intent)
            }
        }

    }


}

