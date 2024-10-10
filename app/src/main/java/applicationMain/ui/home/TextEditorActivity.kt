package applicationMain.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        textKey = intent.getStringExtra("textKey").toString() // Hier den Schlüssel für den Eintrag erhalten
        isPrivate = intent.getBooleanExtra("isPrivate", true)

        loadDataFromFirebase()

        binding.saveButton.setOnClickListener {
            saveChangesToFirebase() // Speichern der Änderungen
        }

        binding.returnLogin2.setOnClickListener {
            finish() // Zurück zur vorherigen Aktivität
        }

        binding.dateEditor.setOnClickListener {
            showDatePicker() // Öffnen des Date Pickers
        }
    }

    private fun loadDataFromFirebase() {
        val path = if (isPrivate) "private" else "business"
        val itemRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        itemRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataMap = snapshot.value as Map<*, *>
                binding.titleEditor.setText(dataMap["title"] as? String ?: "")
                binding.dateEditor.setText(dataMap["date"] as? String ?: "")
                binding.taxEditor.setText(dataMap["steuer"] as? String ?: "")

                // Setze die Checkboxen
                val steuer = dataMap["steuer"]?.toString()?.toDoubleOrNull() ?: 0.0
                binding.check20.isChecked = steuer == 20.0
                binding.check10.isChecked = steuer == 10.0
                binding.check13.isChecked = steuer == 13.0
            } else {
                Toast.makeText(this, "Eintrag nicht gefunden!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Laden der Daten: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveChangesToFirebase() {
        val newTitle = binding.titleEditor.text.toString()
        val newDate = binding.dateEditor.text.toString()
        val newTax = getSelectedTax()

        if (newTitle.isEmpty() || newDate.isEmpty() || newTax.isEmpty()) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }

        // Hier wird das gesamte Objekt aktualisiert
        val updatedData = mapOf(
            "title" to newTitle,
            "date" to newDate,
            "steuer" to newTax
        )

        // Hier den Pfad zu `test` anpassen
        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        entryRef.setValue(updatedData).addOnSuccessListener {
            Toast.makeText(this, "Änderungen erfolgreich gespeichert!", Toast.LENGTH_SHORT).show()
            finish() // Zurück zur vorherigen Ansicht
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getSelectedTax(): String {
        return when {
            binding.check20.isChecked -> "20.0"
            binding.check10.isChecked -> "10.0"
            binding.check13.isChecked -> "13.0"
            else -> "0.0" // Standardwert, falls keine Checkbox ausgewählt ist
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.dateEditor.setText(formattedDate) // Setze das Datum in das Textfeld
        }, year, month, day)

        datePickerDialog.show()
    }
}
