package applicationMain.ui.help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import applicationMain.ui.help.Help
import com.example.semester4.databinding.ActivityHelpSetupBinding

class HelpSetup : AppCompatActivity() {
    private lateinit var binding: ActivityHelpSetupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.Help.setOnClickListener() {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        }
    }
}