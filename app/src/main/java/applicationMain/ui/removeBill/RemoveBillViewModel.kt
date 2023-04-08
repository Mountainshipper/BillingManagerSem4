package applicationMain.ui.removeBill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemoveBillViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Remove Bill"
    }
    val text: LiveData<String> = _text
}