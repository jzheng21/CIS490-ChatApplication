package ksu.cs.edu.chatapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ChatRoom : AppCompatActivity() {

    private val db : FirebaseDatabase = FirebaseDatabase.getInstance()
    private var ref : DatabaseReference = db.getReference()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        ref =  db.getReference("messages").child(this.intent.getStringExtra("courseName"))

        var roomName : String = "Unknow room"
        if(savedInstanceState is Bundle){
            roomName = savedInstanceState.getSerializable("title").toString()
        }
        else{
            var extra = getIntent().getStringExtra("title")
            if(extra is String) roomName = extra
        }

        supportActionBar?.setTitle(roomName)

        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error : DatabaseError) {
                Log.w("Database Error", error?.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(databaseSnapshot : DataSnapshot, p1: String?) {
                if(databaseSnapshot is DataSnapshot){
                    val message = databaseSnapshot.getValue(ChatMessage::class.java)
                    if(message is ChatMessage){
                        val textView = TextView(this@ChatRoom)
                        textView.text = "${message.time} - ${message.author} : ${message.content}"
                        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
                        linearLayout.addView(textView)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun sendMessage(view : View){
        val dateFormat = SimpleDateFormat("EEE MM/dd HH:mmaa")
        val time = dateFormat.format(Date())

        var map = mutableMapOf<String, String>()
        map["time"] = time.toString()
        map["author"] = auth.currentUser?.displayName.toString()
        val textView = findViewById<TextView>(R.id.editText)
        map["content"] = textView.text.toString()
        textView.text = ""
        ref.push().setValue(map)
    }
}
