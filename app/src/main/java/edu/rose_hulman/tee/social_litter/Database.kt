package edu.rose_hulman.tee.social_litter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import org.mindrot.jbcrypt.BCrypt

class Database {

    companion object {

        private const val USERS_COLLECTION = "USERS_COLLECTION"
        private const val MESSAGES_COLLECTION = "MESSAGES_COLLECTION"
        private const val GROUPS_COLLECTION = "GROUPS_COLLECTION"

        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"

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


        fun populateTestData(){
            usersRef.get().addOnSuccessListener { snap->
                deleteAll(snap)
                addUser(User("Eric", "Eric@Eric.er", "hunter2"))
            }

            messageRef.get().addOnSuccessListener { snap->
                deleteAll(snap)
                addMessage(Message("Global", "Eric", "Title", "Body", GeoPoint(0.0,0.0), 1.0, 0))
                addMessage(Message("Global", "Jake", "Title2", "Body2", GeoPoint(0.0,0.0), 1.0, 0))
                addMessage(Message("Global", "Chris", "Runescape", "Test", GeoPoint(0.0,0.0), 1.0, 0))
            }

            groupRef.get().addOnSuccessListener { snap->
                deleteAll(snap)
                addGroup(Group("Global", "Description", true))
            }
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
            data[EMAIL] = user.email
            data[PASSWORD] = BCrypt.hashpw(user.password, BCrypt.gensalt())
            usersRef.add(data)
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
            messageRef.addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                getAllMessagesByGroupName("Global", mapController)
            }
        }

        fun getAllMessagesByGroupName(groupName : String, mapController : MapController){
            messageRef.whereEqualTo(GROUP_NAME, groupName).get().addOnSuccessListener { snap ->  

                val messages = ArrayList<Message>()
                for(data in snap.documents){
                    val message = Message(
                        data[GROUP_NAME] as String,
                        data[ORIGINAL_USER] as String,
                        data[MESSAGE_TITLE] as String,
                        data[MESSAGE_TEXT] as String,
                        data[LOCATION] as GeoPoint,
                        data[RADIUS] as Double,
                        (data[LIKES] as Long).toInt()
                    )

                    messages.add(message)

                    mapController.addMessageMarker(message)
                }
            }
        }

        fun usernameTaken(username : String) : Boolean{
            return false
        }

    }

}