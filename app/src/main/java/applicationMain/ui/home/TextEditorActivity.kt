package applicationMain.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import applicationMain.ui.home.FullScreenImageActivity
import com.bumptech.glide.Glide // Glide for loading images
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

        // Retrieve data passed from the previous activity
        textKey = intent.getStringExtra("textKey").toString() // Receive the key for the entry
        isPrivate = intent.getBooleanExtra("isPrivate", true)

        // Load data from Firebase
        loadDataFromFirebase()

        // Save button listener to save changes to Firebase
        binding.saveButton.setOnClickListener {
            saveChangesToFirebase() // Save changes
        }

        // Return button to finish the activity
        binding.returnLogin2.setOnClickListener {
            finish() // Return to the previous activity
        }

        // Date picker dialog when clicking the date editor
        binding.dateEditor.setOnClickListener {
            showDatePicker() // Open the Date Picker
        }

        // Handle delete entry when dustbin icon is clicked
        binding.deleteIcon.setOnClickListener {
            deleteEntryFromFirebase() // Delete the entry
        }

        // Show bill button to view the image in full screen
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

        // Automatically load the image if available
        if (!imageUrl.isNullOrEmpty()) {
            val imageView: ImageView = binding.loadedImageView
            Glide.with(this)
                .load(imageUrl)
                .into(imageView) // Load image into ImageView
        }
    }

    // Load data from Firebase based on the provided textKey
    private fun loadDataFromFirebase() {
        val path = if (isPrivate) "private" else "business"
        val itemRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        itemRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataMap = snapshot.value as Map<*, *>
                binding.titleEditor.setText(dataMap["title"] as? String ?: "")
                binding.dateEditor.setText(dataMap["date"] as? String ?: "")

                // Retrieve imageUrl and load it into the ImageView if it exists
                imageUrl = dataMap["imageUrl"] as? String
                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(imageUrl)
                        .into(binding.loadedImageView)
                }

            } else {
                Toast.makeText(this, "Eintrag nicht gefunden!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Laden der Daten: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Save the changes made in the title and date fields to Firebase
    private fun saveChangesToFirebase() {
        val newTitle = binding.titleEditor.text.toString()
        val newDate = binding.dateEditor.text.toString()

        // Check if all fields are filled
        if (newTitle.isEmpty() || newDate.isEmpty()) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare the updated data to save
        val updatedData = mapOf(
            "title" to newTitle,
            "date" to newDate
        )

        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        // Save the updated data to Firebase
        entryRef.setValue(updatedData).addOnSuccessListener {
            Toast.makeText(this, "Änderungen erfolgreich gespeichert!", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous view
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Speichern: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Delete the entry from Firebase
    private fun deleteEntryFromFirebase() {
        val path = if (isPrivate) "private" else "business"
        val entryRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(textKey)

        // If an image exists, delete it from Firebase Storage
        if (!imageUrl.isNullOrEmpty()) {
            deleteImageFromFirebaseStorage(imageUrl!!)
        }

        // Delete the entry from the database
        entryRef.removeValue().addOnSuccessListener {
            Toast.makeText(this, "Eintrag erfolgreich gelöscht!", Toast.LENGTH_SHORT).show()
            finish() // Return to the previous view after deletion
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Löschen des Eintrags: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Show the date picker dialog for selecting a date
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

    // Delete image from Firebase Storage
    private fun deleteImageFromFirebaseStorage(imageUrl: String) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)

        storageReference.delete().addOnSuccessListener {
            // Image deleted successfully
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Fehler beim Löschen des Bildes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
