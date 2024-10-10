package applicationMain.ui.home

import CustomAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.semester4.databinding.FragmentShowbillBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowBillFragment : Fragment() {

    private var _binding: FragmentShowbillBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var setter: String = ""
    private lateinit var adapter: CustomAdapter
    private val dataList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowbillBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")
        Miau(email)

        binding.bbuissness.setOnClickListener {
            Miau(email)
        }

        binding.bprivate.setOnClickListener {
            loadPrivateData(email)
        }

        setupRecyclerView()
        return root
    }

    private fun setupRecyclerView() {
        adapter = CustomAdapter(dataList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    // Firebase: Eintrag löschen
                    val itemKey = dataList[position].substringAfter(": ").substringBefore("\n").trim() // extrahiere den Schlüssel
                    deleteFromFirebase(itemKey)

                    // RecyclerView: Eintrag löschen
                    dataList.removeAt(position)
                    adapter.notifyItemRemoved(position)

                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Eintrag bearbeiten
                    val item = dataList[position]
                    val intent = Intent(requireContext(), TextEditorActivity::class.java)
                    intent.putExtra("textKey", item.substringAfter(": ").substringBefore("\n").trim()) // Schlüssel
                    intent.putExtra("complete", item) // Vollständige Daten
                    intent.putExtra("isPrivate", setter == "private") // Ob es sich um private oder business Einträge handelt
                    startActivity(intent)

                    // Den Swipe rückgängig machen, da der Eintrag nur bearbeitet und nicht gelöscht wird
                    adapter.notifyItemChanged(position)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                // Optional: Hier kannst du visuelle Hinweise für das Swipen (z.B. Icons) hinzufügen
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun deleteFromFirebase(itemKey: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email.toString().replace(".", "_").replace("#", "").replace("$", "").replace("[", "").replace("]", "")

        // Prüfen, ob "private" oder "business" bearbeitet wird
        val path = if (setter == "private") {
            "private"
        } else {
            "business"
        }

        val itemRef = FirebaseDatabase.getInstance().getReference("users").child(email).child(path).child(itemKey)

        itemRef.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Eintrag erfolgreich gelöscht!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Fehler beim Löschen des Eintrags: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e(TAG, "Fehler beim Löschen des Eintrags", e)
        }
    }

    private fun loadPrivateData(email: String) {
        database = FirebaseDatabase.getInstance().getReference("users").child(email).child("private")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()) {
                    var counter = 0
                    for (childSnapshot in snapshot.children) {
                        val childKey = childSnapshot.key
                        val childValue = childSnapshot.getValue()
                        var value = "\n${++counter}: $childKey\n    ${childValue.toString().replace("{", "").replace("}", " ").replace(",", "\n   ").replace("=", "                                                ").replace("steuer", "tax:   ").replace("date", "date: ").replace("title", "title: ")}"
                        value = value.substringBefore("title")
                        dataList.add(value)
                    }
                    setter = "private"
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun Miau(email: String) {
        database = FirebaseDatabase.getInstance().getReference("users").child(email).child("business")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if (snapshot.exists()) {
                    var counter = 0
                    for (childSnapshot in snapshot.children) {
                        val childKey = childSnapshot.key
                        val childValue = childSnapshot.getValue()
                        var value = "\n${++counter}: $childKey\n    ${childValue.toString().replace("{", "").replace("}", " ").replace(",", "\n   ").replace("=", "                                                ").replace("steuer", "tax:   ").replace("date", "date: ").replace("title", "title: ")}"
                        value = value.substringBefore("title")
                        dataList.add(value)
                    }
                    setter = "business"
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                Toast.makeText(context, "Failed to read value.", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
