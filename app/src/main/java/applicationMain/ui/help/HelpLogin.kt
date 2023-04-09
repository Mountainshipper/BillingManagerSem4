package applicationMain.ui.help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import applicationMain.ui.help.Help
import com.example.semester4.databinding.ActivityHelpLoginBinding

class HelpLogin : AppCompatActivity() {

    private lateinit var binding: ActivityHelpLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityHelpLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.Help.setOnClickListener() {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }
    }
}