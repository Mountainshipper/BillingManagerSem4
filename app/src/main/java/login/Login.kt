package login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import applicationMain.StartApplication
import applicationMain.ui.help.Help
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
        startActivity(intent)}
    binding.help.setOnClickListener {
        val intent = Intent(this, Help::class.java)
        startActivity(intent)}



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
}