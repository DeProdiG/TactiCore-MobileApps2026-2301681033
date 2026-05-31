package com.example.tacticore

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tacticore.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.core.content.edit
import com.example.tacticore.data.AuthRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var sharedPrefs: SharedPreferences
    private val topLevelDestinations = setOf(
        R.id.heroListFragment,
        R.id.allBuildsFragment,
        R.id.counterFragment,
        R.id.contactsFragment,
        R.id.aboutFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE)
        // Зареждаме запомнения режим
        val savedMode = sharedPrefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedMode)
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.heroListFragment -> {
                    navController.navigate(R.id.heroListFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.allBuildsFragment -> {
                    navController.navigate(R.id.allBuildsFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.counterFragment -> {
                    navController.navigate(R.id.counterFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.contactsFragment -> {
                    navController.navigate(R.id.contactsFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.aboutFragment -> {
                    navController.navigate(R.id.aboutFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_dark_mode -> {
                    val current = AppCompatDelegate.getDefaultNightMode()
                    val new = when (current) {
                        AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
                        AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        else -> AppCompatDelegate.MODE_NIGHT_NO
                    }
                    AppCompatDelegate.setDefaultNightMode(new)
                    getSharedPreferences("settings", MODE_PRIVATE).edit {
                        putInt(
                            "night_mode",
                            new
                        )
                    }
                    recreate()
                    true
                }
                R.id.nav_register -> {
                    // Пренасочваме към RegisterFragment
                    navController.navigate(R.id.registerFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_account -> {
                    navController.navigate(R.id.accountFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("Изход")
                        .setMessage("Сигурни ли сте, че искате да излезете от акаунта си?")
                        .setPositiveButton("Да") { _, _ ->
                            val authRepo = AuthRepository(this)
                            authRepo.logout()
                            navController.navigate(R.id.loginFragment)
                            drawerLayout.closeDrawer(GravityCompat.START)
                        }
                        .setNegativeButton("Не", null)
                        .show()
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportInvalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val currentDestination = navController.currentDestination
        val showMenuButton = currentDestination != null && !topLevelDestinations.contains(
            currentDestination.id
        )
        menu?.findItem(R.id.action_open_drawer)?.isVisible = showMenuButton
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open_drawer) {
            showRightMenu()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun showRightMenu() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // премахва заглавието
        dialog.setContentView(R.layout.dialog_right_menu)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // премахва затъмняването
        dialog.window?.attributes?.gravity = Gravity.TOP or Gravity.END
        dialog.window?.attributes?.y = binding.toolbar.height / 2
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<TextView>(R.id.menu_home).setOnClickListener {
            navController.navigate(R.id.heroListFragment)
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.menu_all_builds).setOnClickListener {
            navController.navigate(R.id.allBuildsFragment)
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.menu_counter).setOnClickListener {
            navController.navigate(R.id.counterFragment)
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.menu_contacts).setOnClickListener {
            navController.navigate(R.id.contactsFragment)
            dialog.dismiss()
        }
        dialog.findViewById<TextView>(R.id.menu_about).setOnClickListener {
            navController.navigate(R.id.aboutFragment)
            dialog.dismiss()
        }
        dialog.show()
    }
}
