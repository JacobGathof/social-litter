package edu.rose_hulman.tee.social_litter

import android.app.AlertDialog
import android.util.Log
import android.widget.EditText
import com.google.firebase.firestore.*
import org.mindrot.jbcrypt.BCrypt

class Database {

    companion object {

        private const val USERS_COLLECTION = "USERS_COLLECTION"
        private const val MESSAGES_COLLECTION = "MESSAGES_COLLECTION"
        private const val GROUPS_COLLECTION = "GROUPS_COLLECTION"

        private const val USERNAME = "username"
        private const val USER_GROUP_LIST = "user_groups"

        private const val GROUP_NAME = "groupName"
        private const val GROUP_DESC = "groupDesc"
        private const val GROUP_PRIVACY = "groupPrivacy"

        private const val ORIGINAL_USER = "originalUser"
        private const val MESSAGE_TITLE = "messageTitle"
        private const val MESSAGE_TEXT = "messageText"
        private const val LOCATION = "location"
        private const val RADIUS = "radius"
        private const val LIKES = "likes"


        //private val databaseRef = FirebaseFirestore.getInstance()
        private val usersRef = FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
        private val messageRef = FirebaseFirestore.getInstance().collection(MESSAGES_COLLECTION)
        private val groupRef = FirebaseFirestore.getInstance().collection(GROUPS_COLLECTION)


        private var groupMessageMap = HashMap<String, ArrayList<Message>>()
        private var user : User? = null

        fun populateTestData(){
//            usersRef.get().addOnSuccessListener { snap->
//                deleteAll(snap)
//                //addUser(User("Eric", arrayListOf("Global", "NotGlobal")))
//            }
//
//            messageRef.get().addOnSuccessListener { snap->
//                deleteAll(snap)
//                addMessage(Message("Global", "Eric", "Title", "Body", GeoPoint(4.0,4.0), 1.0, 0))
//                addMessage(Message("Global", "Jake", "Title2", "Body2", GeoPoint(0.0,4.0), 1.0, 0))
//                addMessage(Message("Global", "Chris", "Runescape", "Test", GeoPoint(0.0,0.0), 1.0, 0))
//
//                addMessage(Message("NotGlobal", "Chris", "Runescape", "Test", GeoPoint(7.0,0.0), 1.0, 0))
//                addMessage(Message("NotGlobal", "Chris", "Runescape", "Test", GeoPoint(0.0,7.0), 1.0, 0))
//                addMessage(Message("NotGlobal", "Chris", "Runescape", "Test", GeoPoint(-7.0,0.0), 1.0, 0))
//            }
//
//            groupRef.get().addOnSuccessListener { snap->
//                deleteAll(snap)
//                addGroup(Group("Global", "Description", true))
//            }

        }

        private fun deleteAll(snap: QuerySnapshot){
            for(doc in snap.documents){
                doc.reference.delete()
            }
        }

        fun addGroup(group : Group){
            var data = HashMap<String, Any>()
            data[GROUP_NAME] = group.groupName
            data[GROUP_DESC] = group.description
            data[GROUP_PRIVACY] = group.public
            groupRef.add(data)
        }

        fun addUser(user : User){
            var data = HashMap<String, Any>()
            data[USERNAME] = user.username
            data[USER_GROUP_LIST] = user.groups
            usersRef.document(user.uid).set(data)
        }

        fun updateUser(user : User){
            var data = HashMap<String, Any>()
            data[USERNAME] = user.username
            data[USER_GROUP_LIST] = user.groups
            usersRef.document(user.uid).set(data)
        }

        fun addMessage(message : Message){
            val data = HashMap<String, Any>()

            data[GROUP_NAME] = message.groupName
            data[ORIGINAL_USER] = message.originalUser
            data[MESSAGE_TITLE] = message.messageTitle
            data[MESSAGE_TEXT] = message.messageText
            data[LOCATION] = message.location
            data[RADIUS] = message.radius
            data[LIKES] = message.likes

            messageRef.add(data)
        }


        fun addNewMessageListener(mapController : MapController){
            messageRef.addSnapshotListener{snap, _ ->
                if(snap != null) {
                    for (doc in snap.documentChanges) {
                        val groupName = doc.document[GROUP_NAME] as String
                        val message = createMessageFromSnapshot(doc.document)
                        addMessageToMap(groupName, message)

                        mapController.updateMessageMap(groupName, message)
                    }
                }
            }
        }


        fun getAllMessagesForAllGroups(mapController: MapController){
            messageRef.get().addOnSuccessListener { snap ->
                for(data in snap.documents){

                    val groupName = data[GROUP_NAME] as String
                    val message = createMessageFromSnapshot(data)
                    addMessageToMap(groupName, message)

                }
                mapController.updateMessageMap()
            }
        }

        fun addMessageToMap(groupName: String, message : Message){
            if(groupMessageMap.containsKey(groupName)){
                groupMessageMap[groupName]!!.add(message)
            }else{
                groupMessageMap[groupName] = ArrayList()
                groupMessageMap[groupName]!!.add(message)
            }
        }


        fun getMessageList(key : String) : ArrayList<Message>? {
            return groupMessageMap[key]
        }

        fun getAllMessagesByGroupName(groupName : String, mapController : MapController){

            messageRef.whereEqualTo(GROUP_NAME, groupName).get().addOnSuccessListener { snap ->

                val messages = ArrayList<Message>()
                for(data in snap.documents){

                    val message = createMessageFromSnapshot(data)
                    messages.add(message)

                    mapController.addMessageMarker(message)
                }
            }
        }


        private fun createMessageFromSnapshot(data : DocumentSnapshot) : Message{
            return Message(
                data[GROUP_NAME] as String,
                data[ORIGINAL_USER] as String,
                data[MESSAGE_TITLE] as String,
                data[MESSAGE_TEXT] as String,
                data[LOCATION] as GeoPoint,
                data[RADIUS] as Double,
                (data[LIKES] as Long).toInt()
            )
        }

        fun setUser(uid : String, activity: MainActivity){
            usersRef.document(uid).get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.exists()) {
                    user = createUserFromSnapshot(documentSnapshot)
                    activity.swapToMap()
                }else{
                    var dialog = AlertDialog.Builder(activity)
                    var et = EditText(activity)
                    et.setHint("Enter Username")
                    dialog.setTitle("Welcome to Social Litter")
                        .setView(et)
                        .setPositiveButton("Submit") { _, _ ->
                            user = User(et.text.toString(), ArrayList<String>(), uid)
                            addUser(user!!)
                            activity.swapToMap()
                        }
                        .setCancelable(false)
                        .create().show()
                }
            }
        }

        private fun createUserFromSnapshot(data : DocumentSnapshot) : User{
            return User(
                data[USERNAME] as String,
                data[USER_GROUP_LIST] as List<String>,
                data.id
            )
        }

        //TODO: Need to distinguish here on only groups user is in
        fun addMyGroupListener(adapter : MyGroupAdapter) {
            usersRef.document(user!!.uid).addSnapshotListener { snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException? ->
                if (exception == null) {
                    var groups = snapshot?.get(USER_GROUP_LIST) as List<String>
                    user!!.groups = groups;
                    adapter.groups.addAll(groups)
                }
            }
        }

        //TODO: Need to distinguish here on only groups user is not in
        fun addNewGroupListener(adapter : NewGroupAdapter) {
            groupRef.addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception == null) {
                    for (doc in snapshot!!.documentChanges) {
                        var group = createGroupFromSnapshot(doc.document)
                        adapter.add(group)
                    }
                }
            }
        }

        private fun createGroupFromSnapshot(data: DocumentSnapshot) : Group {
            return Group(
                data[GROUP_NAME] as String,
                data[GROUP_DESC] as String,
                data[GROUP_PRIVACY]as Boolean
            )
        }

    }

}