package br.edu.infnet.tasksapp.api

import android.net.Uri
import br.edu.infnet.tasksapp.data.util.AppConstants
import br.edu.infnet.tasksapp.domain.model.TaskDomain
import br.edu.infnet.tasksapp.domain.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAPI private constructor(){

    private object HOLDER {
        val INSTANCE = FirebaseAPI()
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val mStorage: FirebaseStorage = FirebaseStorage.getInstance()
    }

    companion object{
        val instance: FirebaseAPI by lazy { HOLDER.INSTANCE }
        private val auth: FirebaseAuth by lazy { HOLDER.mAuth }
        private val database: FirebaseDatabase by lazy { HOLDER.mDatabase }
        private val storage : FirebaseStorage by lazy { HOLDER.mStorage }
        private val databaseRootRef = database.getReference(AppConstants.DATABASE.USERS)
        private val storageRootRef = storage.getReference(AppConstants.DATABASE.USERS)
    }

    suspend fun currentUser(): FirebaseUser? = withContext(Dispatchers.IO){
        return@withContext auth.currentUser
    }

    suspend fun createUser(user: User) : Task<AuthResult> = withContext(Dispatchers.IO){
        return@withContext auth.createUserWithEmailAndPassword(user.email, user.password)
    }

    suspend fun login(user: User) : Task<AuthResult> = withContext(Dispatchers.IO){
        return@withContext auth.signInWithEmailAndPassword(user.email, user.password)
    }

    suspend fun sendEmailVerification(): Task<Void>?  = withContext(Dispatchers.IO){
        return@withContext auth.currentUser?.sendEmailVerification()
    }

    suspend fun addNewUserOnDatabase(name : String) = withContext(Dispatchers.IO) {
        databaseRootRef.child(auth.currentUser?.uid.toString())
            .child(AppConstants.DATABASE.USER_INFO)
            .child(AppConstants.DATABASE.NAME)
            .setValue(name)
    }

    suspend fun addTask(task : TaskDomain) = withContext(Dispatchers.IO) {
        val mapTask = mapOf(
            AppConstants.DATABASE.TASK_TITLE to task.title,
            AppConstants.DATABASE.TASK_DESCRIPTION to task.description,
        )
        databaseRootRef.child(auth.currentUser?.uid.toString())
            .child(AppConstants.DATABASE.TASK_LIST)
            .child(task.id.toString())
            .updateChildren(mapTask)
    }

    suspend fun deleteTask(task : TaskDomain) = withContext(Dispatchers.IO){
        databaseRootRef.child(auth.currentUser?.uid.toString())
            .child(AppConstants.DATABASE.TASK_LIST).child(task.id.toString()).removeValue()
    }

    suspend fun stateListener() = withContext(Dispatchers.IO){
        return@withContext auth.addAuthStateListener {  }
    }

    suspend fun logoff() = withContext(Dispatchers.IO){
        return@withContext auth.signOut()
    }

    suspend fun resetPassword(email : String) = withContext(Dispatchers.IO){
        return@withContext auth.sendPasswordResetEmail(email)
    }

    suspend fun saveImage(uri : Uri?) = withContext(Dispatchers.IO){
       if(uri != null){
           storageRootRef
               .child(auth.currentUser?.uid.toString())
               .child(AppConstants.DATABASE.IMAGES)
               .child(AppConstants.DATABASE.COVER_PHOTO)
               .putFile(uri)
               .addOnSuccessListener {task ->
                   task.metadata!!.reference!!.downloadUrl
                       .addOnSuccessListener {url ->
                           val imgUrl = url.toString()
                           databaseRootRef
                               .child(auth.currentUser?.uid.toString())
                               .child(AppConstants.DATABASE.USER_INFO)
                               .child(AppConstants.DATABASE.COVER_PHOTO)
                               .setValue(imgUrl)
                       }
               }
       }
    }

    suspend fun getCoverPhotoURL() : String = suspendCoroutine{ continuation ->
        databaseRootRef
            .child(auth.currentUser?.uid.toString())
            .child(AppConstants.DATABASE.USER_INFO)
            .child(AppConstants.DATABASE.COVER_PHOTO)
            .addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val url = snapshot.value.toString()
                            continuation.resume(url)
                        }else{
                            continuation.resume("")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
    }
}