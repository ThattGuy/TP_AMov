package pt.isec.amov.tp.eguide.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil

data class User(val name: String, val email:String, val picture:String?)

fun FirebaseUser.toUser() : User {
    val displayName = this.displayName ?: ""
    val strEmail = this.email ?: "n.d."
    val picture = this.photoUrl?.toString()
    return User(displayName,strEmail,picture)
}

class AuthViewModel : ViewModel() {

    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val cpassword = mutableStateOf("")
    val name = mutableStateOf("")
    val username = mutableStateOf("")
    val user: MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?>
        get() = _error

    // LiveData for authentication status
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    init {
        // Initialize the LiveData based on the current user
        _isAuthenticated.value = FAuthUtil.currentUser != null
    }

    fun createUserWithEmail(name: String, username: String, email: String, password: String, cpassword: String) {
        if (name.isBlank() ||
            username.isBlank() ||
            email.isBlank() ||
            password.isBlank() ||
            cpassword.isBlank()) {
            _error.value = R.string.empty_fields_error.toString()
            return
        }

        // Check if the passwords match
        if (password != cpassword) {
            _error.value = R.string.passwords_dont_match_error.toString()
            return
        }

        // Basic email format check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _error.value = R.string.invalid_email_error.toString()
            return
        }

        // Launching coroutine in viewModelScope for network call
        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null) {
                    // On successful user creation
                    _user.value = FAuthUtil.currentUser?.toUser()
                } else {
                    // On error
                    _error.value = exception.message
                }
            }
        }
    }

    fun signInWithGoogle(token: String) {

        viewModelScope.launch {
            FAuthUtil.signInWithGoogle(token) { exception ->
                if (exception == null) {
                    _user.value = FAuthUtil.currentUser?.toUser()
                    _error.value = null
                } else {
                    _error.value = exception.message
                }
            }
        }
    }


    fun loginWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }

    fun isUserAuthenticated(): Boolean {
        val isAuthenticated = FAuthUtil.currentUser != null
        Log.d("AuthViewModel", "isUserAuthenticated: $isAuthenticated")
        return isAuthenticated
    }

    private fun updateAuthenticationStatus() {
        _isAuthenticated.value = FAuthUtil.currentUser != null
    }
}