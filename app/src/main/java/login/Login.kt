package login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import applicationMain.MainStart
import applicationMain.StartApplication
import applicationMain.ui.help.Help
import com.example.semester4.MainActivity
import com.example.semester4.R
import com.example.semester4.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {


    private lateinit var binding: LoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.switchSignIn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }


        binding.help.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }

        binding.resetPassword.setOnClickListener {
            resetPassword()
        }

        binding.imageView4.setOnClickListener {
            val intent = Intent(this, MainStart::class.java)
            startActivity(intent)
        }


        binding.signUp.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, StartApplication::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }
    override fun onBackPressed() {
        Toast.makeText(this, "You cannot go back from here", Toast.LENGTH_SHORT).show()

    }


    private fun resetPassword() {
        val emailPrompt = EditText(this)
        emailPrompt.hint = "Enter your email"

        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Reset Password")
            .setMessage("Please enter your email to reset your password")
            .setView(emailPrompt)
            .setPositiveButton("Reset") { dialog, which ->
                val email = emailPrompt.text.toString().trim()

                if (email.isNotEmpty()) {
                    val auth = FirebaseAuth.getInstance()

                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

}