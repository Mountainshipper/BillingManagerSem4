package applicationMain.ui.help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semester4.databinding.ActivityHelpBinding
<<<<<<< HEAD:app/src/main/java/login/Help.kt

=======
import com.google.firebase.auth.FirebaseAuth
import login.Login
>>>>>>> a5d4f0d5ab2912e79fb529874c270c86f67e0769:app/src/main/java/applicationMain/ui/help/Help.kt

class Help : AppCompatActivity() {


    private lateinit var binding: ActivityHelpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.HelpLogin.setOnClickListener() {
            val intent = Intent(this, HelpLogin::class.java)
            startActivity(intent)
        }

        binding.HelpSetup.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.HelpAdvanced.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}