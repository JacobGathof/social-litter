package edu.rose_hulman.tee.social_litter

import com.google.firebase.firestore.FirebaseFirestore

class Database {

    private val databaseRef = FirebaseFirestore
        .getInstance()
        .collection(USERS)


    fun populateTestData(){
        databaseRef.add("")

    }

    companion object {
        val USERS = "USERS"
    }

}