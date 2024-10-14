package applicationMain.ui.addBill

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import applicationMain.StartApplication
import com.example.semester4.databinding.AddBillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class AddBillFragment : Fragment() {

    private var _binding: AddBillBinding? = null
    private val binding2 get() = _binding!!
    private lateinit var database: DatabaseReference
    var category: String = ""

    // camera
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    // storage
    private lateinit var storageRef : StorageReference
    private var imageUrl: String? = null
    private var capturedPhoto: Bitmap? = null

    // New ImageView reference
    private lateinit var imageViewCapturedPhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(AddBillViewModel::class.java)

        _binding = AddBillBinding.inflate(inflater, container, false)
        val root: View = binding2.root

        val textView: TextView = binding2.BillTitle
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Initialize ImageView
        imageViewCapturedPhoto = _binding!!.imgCapturedPhoto

        // Use the new DatePicker implementation
        _binding!!.txtDeadline.setOnClickListener {
            showDatePicker()
        }

        _binding?.btnSetWork?.setOnClickListener {
            val title = _binding!!.BillTitle.text.toString()
            val price = _binding!!.txtTitel.text.toString()
            val private = _binding!!.Private.isChecked
            val business = _binding!!.Business.isChecked
            val date = _binding!!.txtDeadline.text.toString()

            if (title.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "Please fill out all necessary forms", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!((!private || business) || (private || !business))) {
                Toast.makeText(context, "Private or business was not selected", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            category = if (private && !business) {
                "private"
            } else if (business && !private) {
                "business"
            } else {
                Toast.makeText(context, "Please select either private or business", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            database = FirebaseDatabase.getInstance().reference
            storageRef = FirebaseStorage.getInstance().reference

            // save
            capturedPhoto?.let {
                uploadPhotoToFirebase(it) { uploadedImageUrl  ->
                    if (uploadedImageUrl  != null) {
                        saveBillToFirebase(title, price, date, uploadedImageUrl )
                    } else {
                        Toast.makeText(context, "Fehler beim Hochladen des Bildes", Toast.LENGTH_SHORT).show()
                    }
                }
            } ?: run {
                // If no image is available, save the invoice without an image
                saveBillToFirebase(title, price, date, null)
            }
        }

        //camera
        _binding?.btnCamera?.setOnClickListener {
            checkPermissionCamera()
        }
        initCameraRequest()

        return root
    }

    private fun saveBillToFirebase(title: String, price: String, date: String, imageUrl: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
        // db
        val bill = Bill(title, price, date, imageUrl)
        database.child("users").child(email).child(category).child(title).setValue(bill)
        Toast.makeText(context, "Bill added", Toast.LENGTH_SHORT).show()

        // Reset
        this.imageUrl = null
        capturedPhoto = null

        val intent = Intent(requireContext(), StartApplication::class.java)
        startActivity(intent)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear)
                _binding!!.txtDeadline.setText(formattedDate) // Set the date in the text field
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // camera & storage

    private fun initCameraRequest(){
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(context, "CAMERA permission required :)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionCamera() {
        context?.let { context ->
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    Toast.makeText(context, "Camera permission is needed to take pictures of bills :-)", Toast.LENGTH_LONG).show()
                }
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            // Temporär speichern
            capturedPhoto = photo

            // Setze das aufgenommene Bild in das ImageView
            imageViewCapturedPhoto.setImageBitmap(photo)

            // Optional: Bild zu Firebase hochladen
            // uploadPhotoToFirebase(photo)
        }
    }

    private fun uploadPhotoToFirebase(photo: Bitmap, onSuccess: (String?) -> Unit){
        // Create a reference to the storage location
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")

        // Convert the image to a byte array
        val baos = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Start Upload
        val uploadTask = storageRef.putBytes(data)

        // check upload
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                onSuccess(imageUrl)
            }.addOnFailureListener {
                onSuccess(null)
                Toast.makeText(context, "Fehler beim Abrufen der Bild-URL", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            onSuccess(null)
        }
    }
}
