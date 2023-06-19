package login

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import applicationMain.MainStart
import applicationMain.StartApplication
import applicationMain.ui.help.Help
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


        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, StartApplication::class.java)
            startActivity(intent)
        }

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
            lateinit var progressDialog: ProgressDialog
            // Create a progress dialog with custom theme
            progressDialog = ProgressDialog(this, R.style.CustomProgressDialog)
            progressDialog.setCancelable(false)
            progressDialog.show()

            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, StartApplication::class.java)
                        startActivity(intent)
                        progressDialog.dismiss()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                    .show()
                progressDialog.dismiss()

            }
        }
    }
    override fun onBackPressed() {
        Toast.makeText(this, "You cannot go back from here", Toast.LENGTH_SHORT).show()

    }


    private fun resetPassword() {
        val customDialogView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val emailPrompt = customDialogView.findViewById<EditText>(R.id.emailPrompt)
        val resetButton = customDialogView.findViewById<Button>(R.id.resetButton)

        val dialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setView(customDialogView)

        val dialog = dialogBuilder.create()

        resetButton.setOnClickListener {
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

            dialog.dismiss()
        }

        dialog.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation // Apply custom dialog window animation

        dialog.show()
    }

}