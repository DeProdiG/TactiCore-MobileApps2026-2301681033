package com.example.tacticore

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Списък с ID-та на "началните" фрагменти (там не искаме допълнителен бутон за меню)
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
                else -> false
            }
        }
        // Следим за промяна на дестинацията, за да обновим менюто
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportInvalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Инфлейтваме менюто само за детайлите – но не го показваме веднага, а ще контролираме видимостта
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // Скриваме или показваме бутона в зависимост от текущата дестинация
        val currentDestination = navController.currentDestination
        val showMenuButton = currentDestination != null && !topLevelDestinations.contains(currentDestination.id)
        menu?.findItem(R.id.action_open_drawer)?.isVisible = showMenuButton
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open_drawer -> {
                // Намираме изгледа на бутона (може да се подаде toolbar или menu item view)
                // Най-лесно: вземаме текущия toolbar
                showRightMenu(binding.toolbar)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun showRightMenu(view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.drawer_menu, popupMenu.menu)
        popupMenu.gravity = android.view.Gravity.END // позициониране вдясно
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.heroListFragment -> navController.navigate(R.id.heroListFragment)
                R.id.allBuildsFragment -> navController.navigate(R.id.allBuildsFragment)
                R.id.counterFragment -> navController.navigate(R.id.counterFragment)
                R.id.contactsFragment -> navController.navigate(R.id.contactsFragment)
                R.id.aboutFragment -> navController.navigate(R.id.aboutFragment)
                else -> false
            }
            true
        }
        popupMenu.show()
    }
}