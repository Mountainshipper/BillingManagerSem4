package applicationMain.ui.help.old

import CustomAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.semester4.databinding.FragmentShowbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowBillFragment : Fragment() {

//    private var _binding: FragmentShowbillBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var database: DatabaseReference
//    private var setter: String = ""
//    private lateinit var adapter: CustomAdapter
//    private val dataList = mutableListOf<String>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentShowbillBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val user = FirebaseAuth.getInstance().currentUser
//        val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
//        loadPrivateData(email)
//
//        binding.bbuissness.setOnClickListener {
//            loadBusinessData(email)
//        }
//
//        binding.bprivate.setOnClickListener {
//            loadPrivateData(email)
//        }
//
//        setupRecyclerView()
//        return root
//    }
//
//    private fun setupRecyclerView() {
//        //adapter = CustomAdapter(dataList)
//        binding.recyclerView.layoutManager = LinearLayoutManager(context)
//        binding.recyclerView.adapter = adapter
//    }
//
//    private fun loadPrivateData(email: String) {
//        database = FirebaseDatabase.getInstance().getReference("users").child(email).child("private")
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                dataList.clear()
//                if (snapshot.exists()) {
//                    var counter = 0
//                    for (childSnapshot in snapshot.children) {
//
//                        val childValue = childSnapshot.getValue() as Map<*, *>
//                        val title = childValue["title"] as? String ?: "Unbekannt"
//                        var value = "\n${++counter}: Title: $title\n    ${childValue["date"]}\n    ${childValue["steuer"]}"
//                        dataList.add(value)
//                    }
//                    setter = "private"
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    private fun loadBusinessData(email: String) {
//        database = FirebaseDatabase.getInstance().getReference("users").child(email).child("business")
//        database.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                dataList.clear()
//                if (snapshot.exists()) {
//                    var counter = 0
//                    for (childSnapshot in snapshot.children) {
//                        val childValue = childSnapshot.getValue() as Map<*, *>
//                        val title = childValue["title"] as? String ?: "Unbekannt"
//                        var value = "\n${++counter}: Title: $title\n    ${childValue["date"]}\n    ${childValue["steuer"]}"
//                        dataList.add(value)
//                    }
//                    setter = "business"
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}
