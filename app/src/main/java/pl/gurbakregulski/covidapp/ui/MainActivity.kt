package pl.gurbakregulski.covidapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.gurbakregulski.covidapp.R
import pl.gurbakregulski.covidapp.databinding.MainActivityBinding
import pl.gurbakregulski.covidapp.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { bind() }
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.updateData {
            showErrorMessage("Data fetching failed. Try again later")
        }

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainerView
        ) as NavHostFragment

        navController = navHostFragment.navController
        val controller = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        controller.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(setOf(R.navigation.map, R.navigation.stats))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun bind() = MainActivityBinding.inflate(layoutInflater).apply {
        lifecycleOwner = this@MainActivity
        viewModel = viewModel
    }

    private fun showErrorMessage(message: String) {
        Snackbar
            .make(
                binding.fragmentContainerView,
                message,
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(binding.bottomNavigation)
            .show()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
    }

}
