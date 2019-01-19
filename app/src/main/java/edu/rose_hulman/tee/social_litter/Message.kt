package edu.rose_hulman.tee.social_litter

import com.google.firebase.firestore.GeoPoint

class Message(
    var groupName : String,
    var originalUser: String,
    var messageTitle : String,
    var messageText : String,
    var location : GeoPoint,
    var radius : Double,
    var likes : Int) {

}