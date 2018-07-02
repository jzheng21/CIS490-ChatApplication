package ksu.cs.edu.chatapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user != null){
            showMessage("Already signed in")
        } else{
            showMessage("Not signed in")
        }

        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("threads")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase Database Error", error?.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.w("Thread Selection Error", "Threads cannot be moved")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                if(dataSnapshot is DataSnapshot) {
                    val discussionThread = dataSnapshot.getValue(DiscussionThread::class.java)
                    if(discussionThread is DiscussionThread) {
                        val view = findViewById<LinearLayout>(R.id.linearLayoutThreads)
                        val button = view.findViewWithTag<Button>(dataSnapshot.key)
                        button.text = discussionThread.title
                    }
                }
                showMessage("Child changed")
            }

            override fun onChildAdded(dataSnapshot : DataSnapshot, p1: String?) {
                showMessage("Child Added")

                if(dataSnapshot is DataSnapshot){
                    val discussionThread = dataSnapshot.getValue(DiscussionThread::class.java)
                    if(discussionThread is DiscussionThread) {
                        val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutThreads)
                        val launchThreadButton = Button(this@SelectionActivity)
                        launchThreadButton.text = discussionThread.title
                        launchThreadButton.tag = dataSnapshot.key
                        launchThreadButton.setOnClickListener {
                            startActivity(Intent(this@SelectionActivity, ChatRoom::class.java).putExtra("title", discussionThread.title))
                        }
                        linearLayout.addView(launchThreadButton)
                    }
                }
            }

            override fun onChildRemoved(dataSnapshot : DataSnapshot) {
                if(dataSnapshot is DataSnapshot) {
                    val view = findViewById<LinearLayout>(R.id.linearLayoutThreads)
                    val button = view.findViewWithTag<Button>(dataSnapshot.key)
                    view.removeView(button)
                }
                showMessage("Child Removed")
            }
        })
    }

    private fun showMessage(message: String){
        val view = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }
}
