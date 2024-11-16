package applicationMain.ui.contacts
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log

class ContactService(private val context: Context) {

    fun loadContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor == null) {
            Log.e("ContactLoader", "Cursor is null. Could not access contacts.")
            return emptyList()
        }

        cursor.use {
            while (it.moveToNext()) {
                try {
                    val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contacts.add(Contact(name, number))
                    Log.d("ContactLoader", "Loaded contact: $name - $number")
                } catch (e: Exception) {
                    Log.e("ContactLoader", "Error reading contact", e)
                }
            }
        }

        return contacts
    }
}

data class Contact (
    val name: String,
    val phoneNumber: String
)
