package applicationMain.ui.removeBill

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.semester4.databinding.FragmentRemovebillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class RemoveBillFragment : Fragment() {

    private var _binding: FragmentRemovebillBinding? = null
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



        binding?.dbusiness?.setOnClickListener {
            val dataList = mutableListOf<String>()
            var counter = 0
            val adapter =
                CustomAdapter(requireActivity(), android.R.layout.simple_list_item_1, dataList)
            binding.DDisplayInfo2.adapter = adapter

            dataList.add("----")

            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("business")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            var value =
                                "\n" + ++counter + ": " + childKey.toString() + "\n    " + childValue.toString()
                                    .replace("{", "")
                                    .replace("}", " ").replace(",", "\n   ")
                                    .replace(
                                        "=", "                                                "
                                    ).replace("steuer", "tax:   ").replace(
                                        "date", "date: "
                                    ).replace("title", "title: ")
                            value =
                                value.substringBefore("title") // Extract the substring before "date"
                            dataList.add(
                                value
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

            binding?.DDisplayInfo2?.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val childValue = dataList[position]
                    var modifiedChildValue =
                        childValue.substringBefore("    date") // Extract the substring before "date"
                    modifiedChildValue =
                        modifiedChildValue.substringAfter(": ") // Extract the substring after ": "
                    modifiedChildValue = modifiedChildValue.trim() // Remove trailing spaces
                    println(modifiedChildValue)
                    _binding?.deleteTitle?.setText(modifiedChildValue)
                }
        }



        binding?.dprivate?.setOnClickListener {
            val dataList = mutableListOf<String>()
            var counter = 0
            val adapter =
                CustomAdapter(requireActivity(), android.R.layout.simple_list_item_1, dataList)
            binding.DDisplayInfo2.adapter = adapter

            dataList.add("----")

            database =
                FirebaseDatabase.getInstance().getReference("users").child(Email).child("private")
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            val childKey = childSnapshot.key // get the child node's key
                            val childValue = childSnapshot.getValue() // get the child node's value
                            var value =
                                "\n" + ++counter + ": " + childKey.toString() + "\n    " + childValue.toString()
                                    .replace("{", "")
                                    .replace("}", " ").replace(",", "\n   ")
                                    .replace(
                                        "=", "                                                "
                                    ).replace("steuer", "tax:   ").replace(
                                        "date", "date: "
                                    ).replace("title", "title: ")
                            value =
                                value.substringBefore("title") // Extract the substring before "date"
                            dataList.add(
                                value
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

            binding?.DDisplayInfo2?.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val childValue = dataList[position]
                    var modifiedChildValue =
                        childValue.substringBefore("    date") // Extract the substring before "date"
                    modifiedChildValue =
                        modifiedChildValue.substringAfter(": ") // Extract the substring after ": "
                    modifiedChildValue = modifiedChildValue.trim() // Remove trailing spaces
                    println(modifiedChildValue)
                    _binding?.deleteTitle?.setText(modifiedChildValue)
                }
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
                            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener() {
                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Could not find: " + delete + " in " + setter,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }




        return root
    }

    class CustomAdapter(
        private val context2: Context,
        private val context: Int,
        private val dataList: MutableList<String>
    ) : BaseAdapter() {

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getItem(position: Int): Any {
            return dataList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context2)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = dataList[position]
            return view
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}