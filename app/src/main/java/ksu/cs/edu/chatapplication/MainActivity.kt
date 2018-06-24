package ksu.cs.edu.chatapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference("threads")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase Database Error", error?.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.w("Thread Selection Error", "Threads cannot be moved")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot : DataSnapshot, p1: String?) {
                if(dataSnapshot is DataSnapshot){
                    val discussionThread = dataSnapshot.getValue(DiscussionThread::class.java)
                    if(discussionThread is DiscussionThread) {
                        val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutThreads)
                        val launchThreadButton = Button(this@MainActivity)
                        launchThreadButton.text = discussionThread.title
                        linearLayout.addView(launchThreadButton)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
