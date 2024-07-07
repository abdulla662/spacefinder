package com.example.spacefinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class authnicationpage : AppCompatActivity() {
    lateinit var sendotp: Button
    lateinit var enter: Button
    lateinit var phonenumber2: EditText
    lateinit var OTP2: EditText
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var dialog: AlertDialog
    lateinit var vertificationid: String
    lateinit var uid:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authnicationpage)

        sendotp = findViewById(R.id.sendotp)
        enter = findViewById(R.id.enter)
        phonenumber2 = findViewById(R.id.phonenumber2)
        OTP2 = findViewById(R.id.OTP2)

        auth = FirebaseAuth.getInstance()

        checkCurrentUserAndNavigate()

        val builder = AlertDialog.Builder(this)

        // Initially, the enter button is invisible
        enter.visibility = View.INVISIBLE

        sendotp.setOnClickListener {
            val phoneNumber = "+20" + phonenumber2.text.trim().toString()
            builder.setMessage("please wait...")
            builder.setTitle("loading")
            builder.setCancelable(false)
            dialog = builder.create()
            dialog.show()
            enter.isEnabled = false

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        // Show enter button when verification is completed
                        enter.visibility = View.VISIBLE
                        enter.isEnabled = true
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        enter.isEnabled = false
                        sendotp.isEnabled = true
                        dialog.dismiss()
                        Toast.makeText(this@authnicationpage, "please try again ${p0}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        sendotp.visibility = View.INVISIBLE
                        sendotp.isEnabled = false
                        dialog.dismiss()
                        vertificationid = p0
                        // Uncommented the following line to show the enter button
                        enter.visibility = View.VISIBLE
                        enter.isEnabled = true
                        database = FirebaseDatabase.getInstance().getReference("users")
                        uid = auth.currentUser?.uid.toString()
                        database.child(uid).setValue("").addOnSuccessListener {

                        }.addOnFailureListener {

                        }
                        sendotp.isEnabled = false
                    }
                }).build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            dialog.show()
        }

        enter.setOnClickListener {
            if (OTP2.text!!.isEmpty()) {
                Toast.makeText(this, "please enter otp", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(vertificationid, OTP2.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            dialog.dismiss()
                            sendotp.isEnabled = false
                            enter.isEnabled = true
                            checkCurrentUserAndNavigate()
                        } else {
                            dialog.dismiss()
                            enter.isEnabled = false
                            Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun checkCurrentUserAndNavigate() {
        val currentUser = auth.currentUser?.phoneNumber
        if (currentUser != null) {
            val intent = Intent(this, cars::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun enter(view: View) {
        val intent = Intent(this, cars::class.java)
        startActivity(intent)
    }
}
