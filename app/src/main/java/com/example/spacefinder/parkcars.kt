package com.example.spacefinder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
class parkcars : AppCompatActivity() {
    lateinit var r1: ImageView
    lateinit var r2: ImageView
    lateinit var r3: ImageView
    lateinit var r4: ImageView
    lateinit var r5: ImageView
    lateinit var r6: ImageView
    lateinit var r7: ImageView
    lateinit var r8: ImageView
    private lateinit var databaseReference: DatabaseReference
    lateinit var b1: Button
    lateinit var b2: Button
    lateinit var b3: Button
    lateinit var b4: Button
    lateinit var b5: Button
    lateinit var b6: Button
    lateinit var b7: Button
    lateinit var b8: Button
    lateinit var database: DatabaseReference
    var reservedSpace= mutableListOf<Int>()
    var currentUserPhoneNumber: String? = null
    var currentuser: String? = null
    var a = 0
    var flag = 0
    var parkspace2 = ""
    private fun setClosedImageAndRetrieveReservedPlaces(
        imageView: ImageView,
        drawableResource: Int
    ) {
        imageView.setImageResource(drawableResource)

        if (parkspace2 == "2") {
            retrieveReservedPlaces()
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parkcars)
        databaseReference = FirebaseDatabase.getInstance().reference
        r1 = findViewById(R.id.r1)
        r2 = findViewById(R.id.r2)
        r3 = findViewById(R.id.r3)
        r4 = findViewById(R.id.r4)
        r5 = findViewById(R.id.r5)
        r6 = findViewById(R.id.r6)
        r7 = findViewById(R.id.r7)
        r8 = findViewById(R.id.r8)
        getAllPlaces()


        fun setClosedImageAndDisableButton(imageView: ImageView, button: Button, drawableResource: Int) {
            imageView.setImageResource(drawableResource)
            button.isEnabled = false
            button.isClickable = false
        }


        b1 = findViewById(R.id.b1)
        b2 = findViewById(R.id.b2)
        b3 = findViewById(R.id.b3)
        b4 = findViewById(R.id.b4)
        b5 = findViewById(R.id.b5)
        b6 = findViewById(R.id.b6)
        b7 = findViewById(R.id.b7)
        b8 = findViewById(R.id.b8)
        currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber
        database = FirebaseDatabase.getInstance().reference
        currentUserPhoneNumber?.let { phoneNumber ->
            val userParkSpaceRef = database.child("user_park_spaces").child(phoneNumber)

            userParkSpaceRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if the data exists
                    if (dataSnapshot.exists()) {
                        // Retrieve the value from the database
                        val parkSpace = dataSnapshot.getValue(Int::class.java)
                        parkspace2 = dataSnapshot.getValue(Int::class.java).toString()

                        // Update the TextView with the retrieved value
                        if (parkSpace != 0) {
                            flag = 1
                        }

                        // Assuming each child under "user_park_spaces" represents a phone number and its reserved space
                        val phoneNumber = dataSnapshot.key // Retrieve the phone number
                        val reservedSpace = dataSnapshot.value // Retrieve the reserved space

                        // You can use these values as needed, for example, display in a TextView
                        val displayText = "Phone Number: $phoneNumber, Reserved Space: $reservedSpace"
                        // Update your UI or log this information

                        when (parkSpace) {
                            1 -> setClosedImageAndDisableButton(r1, b1, R.drawable.oneclosed)
                            2 -> setClosedImageAndDisableButton(r2, b2, R.drawable.twoclosed)
                            3 -> setClosedImageAndDisableButton(r3, b3, R.drawable.threeclosed)
                            4 -> setClosedImageAndDisableButton(r4, b4, R.drawable.fourclosed)
                            5 -> setClosedImageAndDisableButton(r5, b5, R.drawable.fiveclosed)
                            6 -> setClosedImageAndDisableButton(r6, b6, R.drawable.sixclosed)
                            7 -> setClosedImageAndDisableButton(r7, b7, R.drawable.sevenclosed)
                            8 -> setClosedImageAndDisableButton(r8, b8, R.drawable.eightclosed)
                        }

                    }


                }
                private fun setClosedImageAndDisableButton(imageView: ImageView, button: Button, drawableResource: Int) {
                    imageView.setImageResource(drawableResource)
                    button.isEnabled = false
                    button.isClickable = false  // Disable the button
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Toast.makeText(this@parkcars, "Failed to read data", Toast.LENGTH_SHORT).show()
                }
            })

        }


        fun handleButtonPress(
            buttonNumber: Int,
            drawableResource: Int,
            imageView: ImageView,
            button: Button
        ) {
            if (!button1Pressed && !button2Pressed && !button3Pressed && !button4Pressed && !button5Pressed
                && !button6Pressed && !button7Pressed && !button8Pressed
            ) {
                when (buttonNumber) {
                    1 -> button1Pressed = true
                    2 -> button2Pressed = true
                    3 -> button3Pressed = true
                    4 -> button4Pressed = true
                    5 -> button5Pressed = true
                    6 -> button6Pressed = true
                    7 -> button7Pressed = true
                    8 -> button8Pressed = true
                }

                a += 1


                val currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber

                if (currentUserPhoneNumber != null) {
                    val userParkSpaceRef =
                        FirebaseDatabase.getInstance().getReference("user_park_spaces")
                            .child(currentUserPhoneNumber)
                    userParkSpaceRef.setValue(buttonNumber)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG)
                                    .show()
                                markSpaceAsReserved(buttonNumber)
                            } else {
                                Toast.makeText(this, "Failed to update", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "User not authenticated", Toast.LENGTH_LONG).show()
                }

                imageView.isEnabled = false
                imageView.setImageResource(drawableResource)
                button.isEnabled = false

                val intent = Intent(this, enjoy::class.java)
                startActivity(intent)
            }
        }


    }



    var button1Pressed = false
    var button2Pressed = false
    var button3Pressed = false
    var button4Pressed = false
    var button5Pressed = false
    var button6Pressed = false
    var button7Pressed = false
    var button8Pressed = false

    fun retrieveReservedPlaces() {
        val reservedPlacesRef = databaseReference.child("reserved_places")

        reservedPlacesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val parkSpace = snapshot.key?.toIntOrNull()
                    val isReserved = snapshot.value as? Boolean
                    if (parkSpace != null && isReserved != null && isReserved) {
                        choosenplaces(parkSpace)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@parkcars,
                    "Failed to read reserved places",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun markSpaceAsReserved(parkSpace: Int) {
        val reservedPlacesRef =
            databaseReference.child("reserved_places").child(parkSpace.toString())
        reservedPlacesRef.setValue(true)
    }
    fun res1(view: View) {
        if (flag == 0) {
            handleButtonPress(1, R.drawable.oneclosed, r1, b1)
        }
    }

    fun res2(view: View) {
        if (flag == 0) {
            handleButtonPress(2, R.drawable.twoclosed, r2, b2)
        }

    }

    fun res3(view: View) {
        if (flag == 0) {
            handleButtonPress(3, R.drawable.threeclosed, r3, b3)
        }

    }

    fun res4(view: View) {
        if (flag == 0) {
            handleButtonPress(4, R.drawable.fourclosed, r4, b4)
        }

    }

    fun res5(view: View) {
        if (flag == 0) {
            handleButtonPress(5, R.drawable.fiveclosed, r5, b5)
        }
    }

    fun res6(view: View) {
        if (flag == 0) {
            handleButtonPress(6, R.drawable.sixclosed, r6, b6)
        }
    }

    fun res7(view: View) {
        if (flag == 0) {
            handleButtonPress(7, R.drawable.sevenclosed, r7, b7)
        }
    }

    fun res8(view: View) {
        if (flag == 0) {
            handleButtonPress(8, R.drawable.eightclosed, r8, b8)
        }
    }

    private fun handleButtonPress(
        buttonNumber: Int,
        drawableResource: Int,
        imageView: ImageView,
        button: Button
    ) {
        if (!button1Pressed && !button2Pressed && !button3Pressed && !button4Pressed && !button5Pressed && !button6Pressed && !button7Pressed && !button8Pressed) {
            when (buttonNumber) {
                1 -> button1Pressed = true
                2 -> button2Pressed = true
                3 -> button3Pressed = true
                4 -> button4Pressed = true
                5 -> button5Pressed = true
                6 -> button6Pressed = true
                7 -> button7Pressed = true
                8 -> button8Pressed = true
            }

            a += 1

            val currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber

            if (currentUserPhoneNumber != null) {
                val userParkSpaceRef =
                    FirebaseDatabase.getInstance().getReference("user_park_spaces")
                        .child(currentUserPhoneNumber)
                userParkSpaceRef.setValue(buttonNumber)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, "Failed to update", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_LONG).show()
            }

            // Update views related to the pressed button
            imageView.isEnabled = false
            imageView.setImageResource(drawableResource)
            button.isEnabled = false

            // Navigate to the next activity if needed
            val intent = Intent(this, enjoy::class.java)
            startActivity(intent)
        }

    }


    fun av(view: View) {
        val reservedSpaces = listOf(b1, b2, b3, b4, b5, b6, b7, b8)

        for ((index, button) in reservedSpaces.withIndex()) {
            if (button.isEnabled) {
                val parkSpaceNumber = index + 1
                checkSpaceReserved(parkSpaceNumber, button)
            }
        }
    }

    private fun checkSpaceReserved(parkSpaceNumber: Int, button: Button) {
        val reservedPlacesRef = databaseReference.child("reserved_places").child(parkSpaceNumber.toString())

        reservedPlacesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isReserved = dataSnapshot.getValue(Boolean::class.java)
                if (isReserved == true) {
                    // Space is reserved, do not trigger the flash animation
                } else {
                    // Space is not reserved, trigger the flash animation
                    YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(button)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Toast.makeText(this@parkcars, "Failed to check if space is reserved", Toast.LENGTH_SHORT).show()
            }
        })

        if (b1.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b1)
        }
        if (b2.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b2)
        }
        if (b3.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b3)
        }
        if (b4.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b4)
        }
        if (b5.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b5)
        }
        if (b6.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b6)
        }
        if (b7.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b7)
        }
        if (b8.isEnabled) {
            YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(b8)
        }
    }

    fun getAllPlaces() {
        val placesRef = FirebaseDatabase.getInstance().getReference("user_park_spaces")

        placesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allPlacesRef = FirebaseDatabase.getInstance().getReference("all_places")

                val stringBuilder = StringBuilder()

                for (childSnapshot in dataSnapshot.children) {
                    val phoneNumber = childSnapshot.key
                    val reservedSpaceValue = childSnapshot.value.toString().toInt()

                    if (phoneNumber != null && reservedSpaceValue != null) {
                        stringBuilder.append("Phone Number: $phoneNumber, Reserved Space: $reservedSpaceValue\n")
                        allPlacesRef.child(phoneNumber).setValue(reservedSpaceValue)
                        reservedSpace.add(reservedSpaceValue)
                        for (space in reservedSpace) {
                            if (reservedSpace.contains(1)) {
                                setClosedImageAndDisableButton(r1, b1, R.drawable.oneclosed)
                            } else if (reservedSpace.contains(2)) {
                                setClosedImageAndDisableButton(r2, b2, R.drawable.twoclosed)
                            } else if (reservedSpace.contains(3)) {
                                setClosedImageAndDisableButton(r3, b3, R.drawable.threeclosed)
                            } else if (reservedSpace.contains(4)) {
                                setClosedImageAndDisableButton(r4, b4, R.drawable.fourclosed)
                            } else if (reservedSpace.contains(5)) {
                                setClosedImageAndDisableButton(r5, b5, R.drawable.fiveclosed)
                            } else if (reservedSpace.contains(6)) {
                                setClosedImageAndDisableButton(r6, b6, R.drawable.sixclosed)
                            } else if (reservedSpace.contains(7)) {
                                setClosedImageAndDisableButton(r7, b7, R.drawable.sevenclosed)
                            } else if (reservedSpace.contains(8)) {
                                setClosedImageAndDisableButton(r8, b8, R.drawable.eightclosed)
                            }
                        }

                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Toast.makeText(this@parkcars, "Failed to read data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setClosedImageAndDisableButton(imageView: ImageView, button: Button, drawableResource: Int) {
        imageView.setImageResource(drawableResource)  // Set the image resource of the ImageView
        button.isEnabled = false                      // Disable the button
        button.isClickable = false                    // Make the button not clickable
    }


    fun back(view: View) {
        val a=Intent(this,cars::class.java)
        startActivity(a)
    }


    fun choosenplaces(parkSpace: Int) {
        when (parkSpace) {
            1 -> setClosedImage(r1, b1)
            2 -> setClosedImage(r2, b2)
            3 -> setClosedImage(r3, b3)
            4 -> setClosedImage(r4, b4)
            5 -> setClosedImage(r5, b5)
            6 -> setClosedImage(r6, b6)
            7 -> setClosedImage(r7, b7)
            8 -> setClosedImage(r8, b8)
            else -> {
            }

        }

    }
    private fun setClosedImage(imageView: ImageView, button: Button) {
        imageView.setImageResource(R.drawable.oneclosed)
        button.isEnabled = false
    }
}