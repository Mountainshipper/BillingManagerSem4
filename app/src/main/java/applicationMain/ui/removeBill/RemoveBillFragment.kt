package applicationMain.ui.removeBill

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.semester4.databinding.FragmentRemovebillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RemoveBillFragment : Fragment() {

    private var _binding: FragmentRemovebillBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var setter: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(RemoveBillViewModel::class.java)

        _binding = FragmentRemovebillBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString()
        val Email = email.replace(".", "_").replace("#", "").replace("$", "").replace("[", "")
            .replace("]", "")


        binding?.dbusiness?.setOnClickListener() {
            binding.DDisplayInfo.text =
                "-------------------------------------------------------------------------\n\n"
            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("business")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            binding.DDisplayInfo.text =
                                binding.DDisplayInfo.text.toString() + childKey.toString() + "\n" + childValue.toString()
                                    .replace("{", "").replace("}", "") + "\n\n"
                            val email = user?.email.toString()
                        }
                        binding.DDisplayInfo.text =
                            binding.DDisplayInfo.text.toString() + "-------------------------------------------------------------------------\n\n"
                        setter = "business"
                    } else {
                        // handle the case where the node does not exist
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
        }

        //var text : Text?= null
        _binding?.dprivate?.setOnClickListener() {
            binding.DDisplayInfo.text =
                "-------------------------------------------------------------------------\n\n"
            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("private")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            binding.DDisplayInfo.text =
                                binding.DDisplayInfo.text.toString() + childKey.toString() + "\n" + childValue.toString()
                                    .replace("{", "").replace("}", "") + "\n\n"
                            val email = user?.email.toString()
                        }
                        binding.DDisplayInfo.text =
                            binding.DDisplayInfo.text.toString() + "-------------------------------------------------------------------------\n\n"
                        setter = "private"
                    } else {
                        // handle the case where the node does not exist
                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
        }

        _binding?.delete?.setOnClickListener() {

            val delete = _binding?.deleteTitle?.text.toString()
            if (delete.isNotEmpty()) {
                database = FirebaseDatabase.getInstance().getReference("users")
                if (setter == "private") {
                    database.child(Email).child("private").child(delete).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                        }
                } else if (setter == "business") {
                    database.child(Email).child("business").child(delete).removeValue()
                        .addOnSuccessListener {
                        }.addOnFailureListener() {
                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show()
                        }
                }else {
                    Toast.makeText(context, "Could not find: " + delete + " in " + setter, Toast.LENGTH_LONG).show()
                }
            }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}