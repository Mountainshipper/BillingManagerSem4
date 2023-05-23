package applicationMain.ui.home

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.Toast
import com.example.semester4.R
import com.example.semester4.databinding.ActivityTextEditorBinding
import java.io.File
import java.io.FileOutputStream


class TextEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextEditorBinding
    var text = ""
    var textAll = ""
    var text2 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        text = intent.getStringExtra("text").toString()
        textAll = intent.getStringExtra("complete").toString()

        val editableText: Editable = SpannableStringBuilder(text)
        binding.textEditor.text = editableText

        binding.returnLogin2.setOnClickListener {
            finish() // Finish the current activity and return to the previous screen
        }
        binding.saveButton.setOnClickListener {
            text2 = text
            saveTextToFile()
        }

        binding.saveButton2.setOnClickListener {
            text2 = textAll
            saveTextToFile()
        }


    }

    private fun saveTextToFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TITLE, "Bills.txt")
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(text2.toByteArray())
                    Toast.makeText(this, "File saved successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val CREATE_FILE_REQUEST_CODE = 1
    }
}