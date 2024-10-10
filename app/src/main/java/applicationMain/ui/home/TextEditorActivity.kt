package applicationMain.ui.home

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.CheckBox
import android.widget.Toast
import com.example.semester4.R
import com.example.semester4.databinding.ActivityTextEditorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class TextEditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextEditorBinding
    private var textKey = ""
    private var isPrivate = true
    private val user = FirebaseAuth.getInstance().currentUser
    private val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textKey = intent.getStringExtra("textKey").toString()
        isPrivate = intent.getBooleanExtra("isPrivate", true)

        val completeText = intent.getStringExtra("complete").toString()
        setInitialData(completeText)

        binding.saveButton.setOnClickListener {
            saveChangesToFirebase()
        }

        binding.returnLogin2.setOnClickListener {
            finish()
        }

        binding.dateEditor.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setInitialData(completeText: String) {
        val dataMap = completeText.lines().map {
            val split = it.split(":")
            split[0].trim() to split.getOrNull(1)?.trim()
        }.toMap()

        binding.titleEditor.setText(dataMap["title"] ?: "")
        binding.dateEditor.setText(dataMap["date"] ?: "")
        binding.taxEditor.setText(dataMap["steuer"] ?: "")

        // Setze die Checkboxen basierend auf den aktuellen Daten
        val steuer = dataMap["steuer"]?.toDoubleOrNull() ?: 0.0
        binding.check20.isChecked = steuer == 20.0
        binding.check10.isChecked = steuer == 10.0
        binding.check13.isChecked = steuer == 13.0
    }

    private fun saveChangesToFirebase() {
        val newTitle = binding.titleEditor.text.toString()
        val newDate = binding.dateEditor.text.toString()
        val newTax = getSelectedTax()

        if (newTitle.isEmpty() || newDate.isEmpty() || newTax.isEmpty()) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedData = mapOf(
            "title" to newTitle,
            "date" to newDate,
            "steuer" to newTax
        )

        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        entryRef.setValue(updatedData).addOnSuccessListener {
            Toast.makeText(this, "Änderungen erfolgreich gespeichert!", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getSelectedTax(): String {
        return when {
            binding.check20.isChecked -> "20.0"
            binding.check10.isChecked -> "10.0"
            binding.check13.isChecked -> "13.0"
            else -> "0.0" // Default-Wert, falls keine Checkbox ausgewählt ist
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.dateEditor.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
