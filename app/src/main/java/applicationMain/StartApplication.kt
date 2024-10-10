package applicationMain

import android.os.Bundle
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
import applicationMain.ui.addBill.AddBillFragment
import applicationMain.ui.home.ShowBillFragment

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
            if (isCurrentFragmentAddBill()) {
                openShowBillFragment()  // Navigate to ShowBill if the current fragment is AddBill
            } else {
                openAddBillFragment()  // Otherwise, navigate to AddBill
            }
        }

        updateFabIcon()  // Set the correct FAB icon on startup

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

    // Method to check if the current fragment is AddBillFragment
    private fun isCurrentFragmentAddBill(): Boolean {
        val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_start_application)
        return currentFragment is AddBillFragment
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
        if (isCurrentFragmentAddBill()) {
            fab.setImageResource(R.drawable.home)  // Set to home icon
        } else {
            fab.setImageResource(R.drawable.add)   // Set to add icon
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_start_application)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
