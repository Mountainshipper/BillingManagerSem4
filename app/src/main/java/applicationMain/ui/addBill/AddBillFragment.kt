package applicationMain.ui.addBill

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import applicationMain.StartApplication
import com.example.semester4.databinding.FragmentAddbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddBillFragment : Fragment() {

    private var _binding: FragmentAddbillBinding? = null
    private val binding2 get() = _binding!!
    private lateinit var database: DatabaseReference
    var category: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(AddBillViewModel::class.java)

        _binding = FragmentAddbillBinding.inflate(inflater, container, false)
        val root: View = binding2.root

        val textView: TextView = binding2.BillTitle
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

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

            // Save the bill in Firebase
            database = FirebaseDatabase.getInstance().reference
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
            val bill = Bill(title, price, date)
            database.child("users").child(email).child(category).child(title).setValue(bill)
            Toast.makeText(context, "Bill added", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), StartApplication::class.java)
            startActivity(intent)
        }
        return root
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
}
