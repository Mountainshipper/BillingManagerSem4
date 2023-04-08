package applicationMain.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.semester4.databinding.FragmentShowbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowBillFragment : Fragment() {

    private var _binding: FragmentShowbillBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(ShowBillViewModel::class.java)

        _binding = FragmentShowbillBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString()
        val Email = email.replace(".", "_").replace("#", "").replace("$", "").replace("[", "")
            .replace("]", "")


        binding?.bbuissness?.setOnClickListener() {
            binding.DisplayInfo.text = "------------------------------------\n "
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
                    } else {
                        // handle the case where the node does not exist
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        _binding?.bprivate?.setOnClickListener() {
            binding.DisplayInfo.text = "------------------------------------\n"
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
                    } else {
                        // handle the case where the node does not exist
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }



        return root
    }
    private fun readData(s: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString()
        val Email = email.replace(".", "_").replace("#", "").replace("$", "").replace("[", "")
            .replace("]", "")

        database =
            FirebaseDatabase.getInstance().getReference("users").child(Email).child("private")


    }










        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }