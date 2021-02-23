package ca.ajaypatel.shorts.database

import android.content.ContentValues.TAG
import android.util.Log
import ca.ajaypatel.shorts.activities.CreateStory
import ca.ajaypatel.shorts.activities.EditAllUserStories
import ca.ajaypatel.shorts.activities.RegisterActivity
import ca.ajaypatel.shorts.models.Story
import ca.ajaypatel.shorts.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Firestore {

    private val db = FirebaseFirestore.getInstance()

    fun addUser(activity: RegisterActivity, userInfo: User) {
        //users collection name, create if not there
        db.collection("users")
            //document is uid
            .document(userInfo.id)
            //user object into merge SET
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //TOAST
                Log.d(TAG, "DocumentSnapshot added with ID: ${userInfo.id}")

            }
            .addOnFailureListener() {
                //FAIL
                Log.w(TAG, "Error adding document")

            }
    }

    fun addStoryToAllStories(activity: CreateStory, storyInfo: Story) {
        val idTitle = storyInfo.id + "" + storyInfo.title.subSequence(0,3)
        db.collection("AllStories")
            .document(idTitle)
            .set(storyInfo, SetOptions.merge())
            .addOnSuccessListener {
                //TOAST
                Log.d(TAG, "DocumentSnapshot added with ID: ${storyInfo.id}")

            }
            .addOnFailureListener() {
                //FAIL
                Log.w(TAG, "Error adding document")

            }
    }

    fun addStoryToAllStories(activity: EditAllUserStories, storyInfo: Story) {
        val idTitle = storyInfo.id + "" + storyInfo.title.substring(0,3)
        db.collection("AllStories")
            .document(idTitle)
            .set(storyInfo, SetOptions.merge())
            .addOnSuccessListener {
                //TOAST
                Log.d(TAG, "DocumentSnapshot added with ID: ${storyInfo.id}")

            }
            .addOnFailureListener() {
                //FAIL
                Log.w(TAG, "Error adding document")

            }
    }

    fun addStory(activity: CreateStory, storyInfo: Story) {
        db.collection("users").document(storyInfo.id).collection("stories")
            .document(storyInfo.title)
            .set(storyInfo, SetOptions.merge())
            .addOnSuccessListener {
                //TOAST
                Log.d(TAG, "DocumentSnapshot added with ID: ${storyInfo.id}")

            }
            .addOnFailureListener() {
                //FAIL
                Log.w(TAG, "Error adding document")

            }
    }

    fun addEditedStory(activity: EditAllUserStories, storyInfo: Story) {
        db.collection("users").document(storyInfo.id).collection("stories")
            .document(storyInfo.title)
            .set(storyInfo, SetOptions.merge())
            .addOnSuccessListener {
                //TOAST
                Log.d(TAG, "DocumentSnapshot iupdated with ID: ${storyInfo.id}")

            }
            .addOnFailureListener() {
                //FAIL
                Log.w(TAG, "Error updating document")

            }
    }


}