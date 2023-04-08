package applicationMain.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.semester4.databinding.FragmentShowbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

class ShowBillFragment : Fragment() {

    private var _binding: FragmentShowbillBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the ShowBillFragment
     * Implements onClickListener for the buttons. Private and Business
     * Displays the data from the database in the textview
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowbillBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString()
        val Email = email.replace(".", "_").replace("#", "").replace("$", "").replace("[", "")
            .replace("]", "")


        binding?.bbuissness?.setOnClickListener() {
            binding.DisplayInfo.text = "-------------------------------------------------------------------------\n\n"
            database = FirebaseDatabase.getInstance().getReference("users").child(Email).child("business")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            binding.DisplayInfo.text = binding.DisplayInfo.text.toString() + childKey.toString() + "\n" + childValue.toString().replace("{", "").replace("}", "")+"\n\n"
                            val email = user?.email.toString()
                        }
                        binding.DisplayInfo.text = binding.DisplayInfo.text.toString() + "-------------------------------------------------------------------------\n\n"
                    } else {
                        // handle the case where the node does not exist
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
        }

        //var text : Text?= null
        _binding?.bprivate?.setOnClickListener() {
            binding.DisplayInfo.text = "-------------------------------------------------------------------------\n\n"
            database = FirebaseDatabase.getInstance().getReference("users").child(Email).child("private")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            binding.DisplayInfo.text = binding.DisplayInfo.text.toString() + childKey.toString() + "\n" + childValue.toString().replace("{", "").replace("}", "")+"\n\n"
                            val email = user?.email.toString()
                        }
                        binding.DisplayInfo.text = binding.DisplayInfo.text.toString() + "-------------------------------------------------------------------------\n\n"
                    } else {
                        // handle the case where the node does not exist
                    }

                }


                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
        }
        return root
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }