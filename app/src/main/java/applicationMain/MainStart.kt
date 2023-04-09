package applicationMain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.semester4.databinding.ActivityMainBinding
import login.Login

class MainStart : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnLogin2.setOnClickListener() {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}