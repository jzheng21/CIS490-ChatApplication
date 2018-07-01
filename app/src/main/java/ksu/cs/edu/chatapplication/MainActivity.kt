package ksu.cs.edu.chatapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.Arrays.asList





class MainActivity : AppCompatActivity() {
    val RC_SIGN_IN = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder().build(),
                    RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
            else{
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                Log.e("Sign-in error", "Sign-in error", response?.error)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun launchSelections(view : View){
        val intent = Intent(this, SelectionActivity::class.java)
        startActivity(intent)
    }
}
