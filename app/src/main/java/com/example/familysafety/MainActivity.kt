package com.example.familysafety

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_CONTACTS,
    )
    val permsissioncode =78
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askforPermission()
        val bottombar = findViewById<BottomNavigationView>(R.id.bv_bar)
        bottombar.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navgaurd -> {
                    inflatefragment(GaurdFragment.newInstance())
                }
                R.id.navhome -> {

                    inflatefragment(homeFragment.newInstance())
                }
                R.id.navprofile -> {

                    inflatefragment(ProfileFragment.newInstance())
                }
                R.id.navdash -> {

                    inflatefragment(MapsFragment())
                }
            }
            true
        }
        bottombar.selectedItemId=R.id.navhome
        val db=Firebase.firestore
         val currentUser=FirebaseAuth.getInstance().currentUser
        val name=currentUser?.displayName.toString()
        val mail=currentUser?.email.toString()
        val phonenumber=currentUser?.phoneNumber.toString()
        val imageurl =currentUser?.photoUrl.toString()
        val user = hashMapOf(
            "name" to name,
            "mail" to mail,
            "phoneNumber"  to phonenumber,
            "imageUrl" to imageurl

        )
db.collection("users").document(mail).set(user).addOnSuccessListener {

}
    .addOnFailureListener {

    }

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore89", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore89", "Error adding document", e)
            }

    }
    
    private fun askforPermission() {

        ActivityCompat.requestPermissions(this,permissions,permsissioncode)
    }


    private fun inflatefragment(newInstance: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newInstance)
        transaction.commit()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permsissioncode){
            if(allpermissiongranted()){
                    //opencamera()
            }
            else{

            }
        }
    }

    private fun opencamera() {
        /*val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)*/
    }

    private fun allpermissiongranted(): Boolean {
        for(item in permissions)
        {
            if(ContextCompat.checkSelfPermission(this,item)!=PackageManager.PERMISSION_GRANTED)

                return false


        }
        return true

    }
}