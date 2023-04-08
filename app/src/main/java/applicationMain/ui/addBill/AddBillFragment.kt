package applicationMain.ui.addBill

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.semester4.databinding.FragmentAddbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class AddBillFragment : Fragment() {

    private var _binding: FragmentAddbillBinding? = null
    private val binding2 get() = _binding!!
    private lateinit var database: DatabaseReference

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

        _binding?.btnSetWork?.setOnClickListener() {
            val title = _binding!!.BillTitle.text.toString()
            val price = _binding!!.txtTitel.text.toString()
            val date = _binding!!.txtDeadline.text.toString()
            val private = _binding!!.Private.isChecked
            val buisness = _binding!!.Business.isChecked
            val check20 = _binding!!.check20.isChecked
            val check10 = _binding!!.check10.isChecked
            val check13 = _binding!!.check13.isChecked

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

            database = FirebaseDatabase.getInstance().reference
            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid.toString()
            val key = database.child("users").child(uid).child("bills").push().key
            val bill = Bill(title, price, date, private, buisness, check20, check10, check13)
            database.child("users").child(uid).child("bills").child(key!!).setValue(bill)
            Toast.makeText(context, "Bill added", Toast.LENGTH_LONG).show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}