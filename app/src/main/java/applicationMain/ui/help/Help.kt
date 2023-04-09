package applicationMain.ui.help


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semester4.databinding.ActivityHelpBinding
import login.Login


class Help : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.helpLogin.setOnClickListener() {
            val intent = Intent(this, HelpLogin::class.java)
            startActivity(intent)
        }

        binding.helpSetup.setOnClickListener() {
            val intent = Intent(this, HelpSetup::class.java)
            startActivity(intent)
        }

        binding.returnLogin.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}