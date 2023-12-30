package pt.isec.amov.tp.eguide.utils.firebase

import android.util.Log
import androidx.compose.ui.res.stringResource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import pt.isec.amov.tp.eguide.R

class FAuthUtil {
    companion object {
        private val auth by lazy { Firebase.auth }

        val currentUser: FirebaseUser?
            get() = auth.currentUser

        fun createUserWithEmail(
            email: String, password: String,
            onResult: (Throwable?) -> Unit
        ) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signInWithGoogle(token: String, onResult: (Throwable?) -> Unit) {
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    onResult(task.exception)
                }
        }

        fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun updateUserInformation(name: String, email: String, onResult: (Throwable?) -> Unit) {
            val user = auth.currentUser
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onResult(task.exception)
                        return@addOnCompleteListener
                    }

                    user.updateEmail(email)
                        .addOnCompleteListener { emailUpdateTask ->
                            onResult(emailUpdateTask.exception)
                        }
                }
        }

        fun changeUserPassword(newPassword: String, onResult: (Throwable?) -> Unit) {
            val user = auth.currentUser

            user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FAuthUtil", R.string.password_updated.toString())
                    onResult(null)
                } else {
                    onResult(task.exception)
                }
            }
        }

        fun signOut() {
            if (auth.currentUser != null) {
                auth.signOut()
            }
        }
    }
}