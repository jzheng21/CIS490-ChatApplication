package ksu.cs.edu.chatapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class ChatRoom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)



        var roomName : String = "Unknow room"
        if(savedInstanceState is Bundle){
            roomName = savedInstanceState.getSerializable("title").toString()
        }
        else{
            var extra = getIntent().getStringExtra("title")
            if(extra is String) roomName = extra
        }
        var roomTitle = findViewById<TextView>(R.id.chatRoomTitle)
        roomTitle.text = roomName
    }
}
