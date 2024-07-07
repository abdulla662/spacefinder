package com.example.spacefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Context
import android.content.Intent
import android.media.Image
import android.transition.Transition
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
class movecars : AppCompatActivity() {
    lateinit var av: TextView
    lateinit var database: DatabaseReference
    var currentUserPhoneNumber: String? = null
    var currentuser: String? = null
    var a = 0
    var flag = 0
    var parkspace2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movecars)
        database = FirebaseDatabase.getInstance().reference.child("user_park_spaces")
        currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber

        currentUserPhoneNumber?.let { phoneNumber ->
            val userParkSpaceRef =
                database.child(phoneNumber) // Get the reference to the user's specific data
        }
    }

    fun wrong(view: View) {
        val a = Intent(this, cars::class.java)
        startActivity(a)
    }

    fun movecars(view: View) {
        val currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber
        val userParkSpaceRef = database.child(currentUserPhoneNumber.orEmpty())
        val allPlacesRef = FirebaseDatabase.getInstance().getReference("all_places")

        userParkSpaceRef.removeValue().addOnCompleteListener { firstTask ->
            if (firstTask.isSuccessful) {
                // User's space removed successfully
                allPlacesRef.removeValue().addOnCompleteListener { secondTask ->
                    if (secondTask.isSuccessful) {
                        // Successfully removed from "all_places"
                        Toast.makeText(this, "See you later", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle failure to remove from "all_places"
                        Toast.makeText(
                            this,
                            "Failed to update your place status",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    userParkSpaceRef.removeValue().addOnCompleteListener { firstTask ->
                        if (firstTask.isSuccessful) {
                            // User's space removed successfully
                            userParkSpaceRef.removeValue().addOnCompleteListener { secondTask ->
                                if (secondTask.isSuccessful) {
                                    // Successfully removed from "all_places"
                                    Toast.makeText(this, "See you later", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Handle failure to remove from "all_places"
                                    Toast.makeText(
                                        this,
                                        "Failed to update your place status",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                // Navigate to the next activity regardless of the result
                                val intent = Intent(this, cars::class.java)
                                startActivity(intent)
                            }
                        } else {
                            // Handle failure to remove user's space
                            Toast.makeText(
                                this,
                                "Failed to update your place status",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navigate to the next activity regardless of the result
                            val intent = Intent(this, cars::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}




