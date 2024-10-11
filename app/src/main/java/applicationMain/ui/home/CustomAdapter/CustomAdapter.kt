import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semester4.R

class CustomAdapter(
    private val dataList: List<String>,
    private val itemClickListener: (String) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemText.text = dataList[position]
        holder.itemView.setOnClickListener {
            // Trigger the click listener with the clicked item's data
            itemClickListener(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val arrowIcon: ImageView = itemView.findViewById(R.id.arrowIcon)
    }
}
