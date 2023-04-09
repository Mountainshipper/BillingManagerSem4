package applicationMain.ui.addBill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddBillViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Add Bill"
    }
    val text: LiveData<String> = _text
}