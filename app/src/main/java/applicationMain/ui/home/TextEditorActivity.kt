package applicationMain.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import applicationMain.ui.home.FullScreenImageActivity
import com.example.semester4.databinding.ChangeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Calendar

class TextEditorActivity : AppCompatActivity() {

    private lateinit var binding: ChangeBinding
    private var imageUrl: String? = null
    private var textKey = ""
    private var isPrivate = true
    private val user = FirebaseAuth.getInstance().currentUser
    private val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textKey = intent.getStringExtra("textKey").toString() // Receive the key for the entry
        isPrivate = intent.getBooleanExtra("isPrivate", true)

        loadDataFromFirebase()

        binding.saveButton.setOnClickListener {
            saveChangesToFirebase() // Save changes
        }

        binding.returnLogin2.setOnClickListener {
            finish() // Return to the previous activity
        }

        binding.dateEditor.setOnClickListener {
            showDatePicker() // Open the Date Picker
        }

        // Handle delete entry when dustbin icon is clicked
        binding.deleteIcon.setOnClickListener {
            deleteEntryFromFirebase() // Delete the entry
        }

        binding.showBill.setOnClickListener {
            if (imageUrl.isNullOrEmpty()) {
                Toast.makeText(this, "Kein Bild verfügbar", Toast.LENGTH_SHORT).show()
            } else {
                // If the URL is available, start the FullScreenImageActivity
                val intent = Intent(this, FullScreenImageActivity::class.java)
                intent.putExtra("imageUrl", imageUrl)
                startActivity(intent)
            }
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

                // imageUrl
                imageUrl = dataMap["imageUrl"] as? String


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

        if (newTitle.isEmpty() || newDate.isEmpty()) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the entire object
        val updatedData = mapOf(
            "title" to newTitle,
            "date" to newDate
        )

        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        entryRef.setValue(updatedData).addOnSuccessListener {
            Toast.makeText(this, "Änderungen erfolgreich gespeichert!", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous view
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteEntryFromFirebase() {
        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        if (!imageUrl.isNullOrEmpty()) {
            deleteImageFromFirebaseStorage(imageUrl!!)
        }

        entryRef.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Eintrag erfolgreich gelöscht!", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous view after deletion
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Löschen des Eintrags: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.dateEditor.setText(formattedDate) // Set the date in the text field
        }, year, month, day)

        datePickerDialog.show()
    }


    // Delete storage with the URL
    private fun deleteImageFromFirebaseStorage(imageUrl: String) {

        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

        storageReference.delete().addOnSuccessListener {
            // Toast.makeText(this, "Bild erfolgreich gelöscht!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            // Toast.makeText(this, "Fehler beim Löschen des Bildes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


}
