package applicationMain.ui.addBill


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import applicationMain.StartApplication
import com.example.semester4.R
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
    var date2: String = ""


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

        _binding!!.txtDeadline.setOnClickListener() {
            val selectedDateTextView = _binding!!.txtDeadline
            val date = DatePickerDialog(
                this.requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    // Do something with the selected date
                    selectedDateTextView.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                },
                // Set the initial date to today's date
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            date.show()
            // date2 = selectedDateTextView.text.toString()
        }


        _binding?.btnSetWork?.setOnClickListener() {

            val title = _binding!!.BillTitle.text.toString()
            val price = _binding!!.txtTitel.text.toString()

            val private = _binding!!.Private.isChecked
            val buisness = _binding!!.Business.isChecked
            val check20 = _binding!!.check20.isChecked
            val check10 = _binding!!.check10.isChecked
            val check13 = _binding!!.check13.isChecked
            val date = _binding!!.txtDeadline.text.toString()


            if (title.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "please fill out all necessary forms", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (!((!private || buisness) || (private || !buisness))) {
                Toast.makeText(context, "private or business was not selected", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (private && !buisness) {
                category = "private"
            } else if (buisness && !private) {
                category = "business"
            } else if (!private && !buisness) {
                Toast.makeText(context, "private or business was not selected", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            } else if (private && buisness) {
                Toast.makeText(
                    context,
                    "Please only select either private or business",
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            } else {
                Toast.makeText(context, "Unknown error in private or business", Toast.LENGTH_LONG)
                    .show()
            }

            val steuer: String
            if (check20 && !check10 && !check13) {
                steuer = "20"
            } else if (check10 && !check13 && !check20) {
                steuer = "10"
            } else if (check13 && !check10 && !check20) {
                steuer = "13"
            } else {
                steuer = "0"
            }

            database = FirebaseDatabase.getInstance().reference
            val user = FirebaseAuth.getInstance().currentUser
            val email = user?.email.toString()
            val Email = email.replace(".", "_").replace("#", "").replace("$", "").replace("[", "")
                .replace("]", "")
            //  val key = database.child("users").child(uid).child("bills").push().key
            val bill = Bill(title, price, date, steuer)
            database.child("users").child(Email).child(category).child(title).setValue(bill)
            Toast.makeText(context, "Bill added", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), StartApplication::class.java)
            startActivity(intent)
        }
        return root
    }

    private fun ProgressDialog(addBillFragment: AddBillFragment, customProgressDialog: Int) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}