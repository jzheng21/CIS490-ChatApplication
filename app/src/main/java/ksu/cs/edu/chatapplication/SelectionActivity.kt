package ksu.cs.edu.chatapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class SelectionActivity : AppCompatActivity() {

    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference("threads")

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.AddChatRoom) {
            // do something here
            var roomName : String = "Unknown"
            var roomId : String = "Unknown"

            val layout = LinearLayout(this)

            val courseName = EditText(this)
            courseName.setHint("Course Name")
            val courseId = EditText(this)
            courseId.setHint("Course ID")

            layout.addView(courseName)
            layout.addView(courseId)

            val thread = Button(this@SelectionActivity)

            val builder = AlertDialog.Builder(this@SelectionActivity)
            builder.setTitle("Add a Chat Room")
            builder.setView(layout)

            builder.setPositiveButton("OK") { dialog, which ->
                roomName = courseName.text.toString();
                roomId = courseId.text.toString();
                var map = mutableMapOf<String, String>()
                map["title"] = roomName
                ref.push().setValue(map)
            }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

            builder.show()



            /*val linearLayout = findViewById<LinearLayout>(R.id.linearLayoutThreads)

            thread.setOnClickListener {
                val intent = Intent(this@SelectionActivity, ChatRoom::class.java)
                intent.putExtra("title", roomName)

                intent.putExtra("courseName", roomId)

                startActivity(intent)
            }
            linearLayout.addView(thread)*/



        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        supportActionBar?.setTitle("Chat Rooms")

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if(user != null){
            showMessage("Already signed in")
        } else{
            showMessage("Not signed in")
        }



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
                            val intent = Intent(this@SelectionActivity, ChatRoom::class.java)
                            intent.putExtra("title", discussionThread.title)
                            intent.putExtra("courseName", dataSnapshot.key)
                            startActivity(intent)
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
