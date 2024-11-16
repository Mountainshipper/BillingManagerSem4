package applicationMain.ui.contacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxWidth

class Contacts {

    @Composable
    fun ContactPermissionHandler(
        contactService: ContactService,
        onContactsLoaded: (List<Contact>) -> Unit
    ) {
        val context = LocalContext.current
        val activity = context as? Activity // Stellt sicher, dass der Kontext eine Activity ist
        if (activity == null) {
            // Wenn kein gültiger Activity-Kontext verfügbar ist, beende die Funktion
            return
        }

        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Check if the contact permission is granted
        var hasContactPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        // Show rationale dialog if needed
        var showPermissionRationale by remember {
            mutableStateOf(
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_CONTACTS
                ) && !sharedPreferences.getBoolean("permission_rationale_shown", false)
            )
        }

        // Request permission if not granted
        if (!hasContactPermission) {
            if (showPermissionRationale) {
                PermissionRationaleDialog(
                    onDismiss = { showPermissionRationale = false },
                    onRequestPermission = {
                        showPermissionRationale = false
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            CONTACT_PERMISSION_REQUEST_CODE
                        )
                    }
                )
                sharedPreferences.edit().putBoolean("permission_rationale_shown", true).apply()
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    CONTACT_PERMISSION_REQUEST_CODE
                )
            }
        }

        // If permission is granted, load contacts
        if (hasContactPermission) {
            val contacts = contactService.loadContacts()
            onContactsLoaded(contacts)
        }
    }

    /**
     * Displays a dialog to explain why the app needs contact permission.
     *
     * @param onDismiss Called when the dialog is dismissed.
     * @param onRequestPermission Called when the user agrees to grant the permission.
     */
    @Composable
    fun PermissionRationaleDialog(
        onDismiss: () -> Unit,
        onRequestPermission: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Contact Permission Required") },
            text = {
                Text("This app needs access to your contacts to import players from your contacts. Please allow contact access.")
            },
            confirmButton = {
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            },
        )
    }

    companion object {
        const val CONTACT_PERMISSION_REQUEST_CODE = 1001
    }
}
