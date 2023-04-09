package login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semester4.databinding.ActivityHelpBinding


class Help : AppCompatActivity() {


    private lateinit var binding: ActivityHelpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.HelpLogin.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
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