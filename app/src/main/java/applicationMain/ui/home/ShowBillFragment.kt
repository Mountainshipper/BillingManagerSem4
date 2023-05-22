package applicationMain.ui.home

import android.R
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast

import androidx.fragment.app.Fragment

import applicationMain.ui.removeBill.RemoveBillFragment
import com.example.semester4.databinding.FragmentShowbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ShowBillFragment : Fragment() {

    private var _binding: FragmentShowbillBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private var setter: String = ""

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
            val dataList = mutableListOf<String>()
            var counter = 0
            val adapter =
                RemoveBillFragment.CustomAdapter(
                    requireActivity(),
                    R.layout.simple_list_item_1,
                    dataList
                )
            binding.DDisplayInfo.adapter = adapter

            dataList.add("----")

            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("business")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            var value = "\n" + ++counter + ": " + childKey.toString() + "\n    " + childValue.toString().replace("{", "")
                                .replace("}", " ") + "\n"
                            value = value.substringBefore("title") // Extract the substring before "date"
                            dataList.add(value + "\n"
                            )
                        }
                        dataList.add("----")
                        setter = "business"
                    } else {
                        // handle the case where the node does not exist
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
                }
            })
        }



        //var text : Text?= null
        _binding?.bprivate?.setOnClickListener() {
            val dataList = mutableListOf<String>()
            var counter = 0
            val adapter =
                RemoveBillFragment.CustomAdapter(
                    requireActivity(),
                    R.layout.simple_list_item_1,
                    dataList
                )
            binding.DDisplayInfo.adapter = adapter

            dataList.add("----")

            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("private")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            var value = "\n" + ++counter + ": " + childKey.toString() + "\n    " + childValue.toString().replace("{", "")
                                .replace("}", " ") + "\n"
                            value = value.substringBefore("title") // Extract the substring before "date"
                            dataList.add(value + "\n"
                            )
                        }
                        dataList.add("----")
                        setter = "private"
                    } else {
                        // handle the case where the node does not exist
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
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