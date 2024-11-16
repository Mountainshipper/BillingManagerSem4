package applicationMain

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.semester4.R
import com.example.semester4.databinding.StartApplication2Binding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import applicationMain.ui.addBill.AddBillFragment
import applicationMain.ui.home.ShowBillFragment
import login.Login

class StartApplication : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: StartApplication2Binding
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StartApplication2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarStartApplication2.toolbar)

        fab = binding.appBarStartApplication2.fab

        // Set up FAB click listener
        fab.setOnClickListener {
            if (isCurrentFragmentShowBill()) {
                openAddBillFragment()  // Navigate to AddBill if the current fragment is ShowBill
            } else {
                openShowBillFragment()  // Otherwise, navigate to ShowBill
            }
        }

        updateFabIcon()  // Set the correct FAB icon on startup

        // Set up logout button click listener
        val logoutButton: ImageView = findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            logoutUser()  // Call the logout function when clicked
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_start_application)

        // Configure the app bar for navigation
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.add_Bill, R.id.remove_Bill), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // Method to handle user logout
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()  // Close the current activity
    }

    // Method to check if the current fragment is ShowBillFragment
    private fun isCurrentFragmentShowBill(): Boolean {
        val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_start_application)
        return currentFragment is ShowBillFragment
    }

    // Method to open AddBillFragment
    private fun openAddBillFragment() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = AddBillFragment()
        transaction.replace(R.id.nav_host_fragment_content_start_application, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        updateFabIcon() // Update FAB icon after navigation
    }

    // Method to open ShowBillFragment
    private fun openShowBillFragment() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = ShowBillFragment()
        transaction.replace(R.id.nav_host_fragment_content_start_application, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        updateFabIcon() // Update FAB icon after navigation
    }

    // Update the FAB icon based on the current fragment
    private fun updateFabIcon() {
        if (isCurrentFragmentShowBill()) {
            fab.setImageResource(R.drawable.home)  // Set to plus icon when in ShowBill
        } else {
            fab.setImageResource(R.drawable.add)  // Set to home icon when in AddBill
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_start_application)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
