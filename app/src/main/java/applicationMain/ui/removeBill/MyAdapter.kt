package applicationMain.ui.removeBill

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import applicationMain.ui.addBill.Bill
import com.example.semester4.R
import com.google.firebase.database.ValueEventListener


private var ImageView.text: String?
    get() = text.toString()
    set(value) {
        text = value
    }

class MyAdapter(
    private val context: Context,
    private val arrayList: ArrayList<Bill>
) : ArrayAdapter<Bill>(context, R.layout.list_item, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, parent, false)

//        val title: ImageView = view.findViewById(R.id.titleD)
//        val price: ImageView = view.findViewById(R.id.priceD)
//        val steuer: ImageView = view.findViewById(R.id.steuerD)
//        val date: ImageView = view.findViewById(R.id.dateD)



        return view

}
}