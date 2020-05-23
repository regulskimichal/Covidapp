package pl.gurbakregulski.covidapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.blongho.country_data.World
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.databinding.MainActivityBinding
import pl.gurbakregulski.covidapp.setupWithNavController
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { bind() }
    private val viewModel: MainActivityViewModel by viewModel()

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        World.init(this)
        viewModel.updateData {
            Snackbar
                .make(
                    binding.fragmentContainerView,
                    "Data fetching failed. Try again later",
                    Snackbar.LENGTH_LONG
                )
                .setAnchorView(binding.bottomNavigation)
                .show()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.map, R.navigation.stats)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = findViewById<BottomNavigationView>(R.id.bottomNavigation).setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragmentContainerView,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun bind() = MainActivityBinding.inflate(layoutInflater).apply {
        lifecycleOwner = this@MainActivity
        viewModel = viewModel
    }

}
